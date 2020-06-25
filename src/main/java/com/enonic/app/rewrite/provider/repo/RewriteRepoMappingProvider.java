package com.enonic.app.rewrite.provider.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.CreateRuleParams;
import com.enonic.app.rewrite.DeleteRuleParams;
import com.enonic.app.rewrite.provider.RewriteContextExistsException;
import com.enonic.app.rewrite.provider.RewriteContextNotFoundException;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.provider.RewriteRuleException;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
import com.enonic.xp.context.Context;
import com.enonic.xp.context.ContextAccessor;
import com.enonic.xp.context.ContextBuilder;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.FindNodesByParentParams;
import com.enonic.xp.node.FindNodesByParentResult;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeIds;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.node.RefreshMode;
import com.enonic.xp.node.UpdateNodeParams;
import com.enonic.xp.repository.RepositoryService;

import static com.enonic.app.rewrite.provider.repo.RewriteMappingRepoInitializer.BRANCH;

public class RewriteRepoMappingProvider
    implements RewriteMappingProvider
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteRepoMappingProvider.class );

    public final static NodePath MAPPING_ROOT_NODE = NodePath.create( "/vhosts" ).build();

    private final RepositoryService repositoryService;

    private final IndexService indexService;

    private final NodeService nodeService;

    public RewriteRepoMappingProvider( final RepositoryService repositoryService, final IndexService indexService,
                                       final NodeService nodeService )
    {
        this.repositoryService = repositoryService;
        this.indexService = indexService;
        this.nodeService = nodeService;

        LOG.info( "Initializing rewrite-mapping repo" );

        RewriteMappingRepoInitializer.create().
            repositoryService( repositoryService ).
            nodeService( nodeService ).
            setIndexService( indexService ).
            build().
            initialize();
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey contextKey )
    {
        return setRepoContext().callWith( () -> doGetRewriteMapping( contextKey ) );
    }

    private RewriteMapping doGetRewriteMapping( final RewriteContextKey contextKey )
    {
        final Node node = doGetContextNode( contextKey );

        if ( node == null )
        {
            return null;
        }

        return RewriteMappingSerializer.fromNode( node );
    }

    @Override
    public boolean readOnly()
    {
        return false;
    }

    @Override
    public String name()
    {
        return "LocalRepo";
    }

    public RewriteMappings getRewriteMappings()
    {
        return setRepoContext().callWith( this::doGetMappings );
    }

    private RewriteMappings doGetMappings()
    {
        final RewriteMappings.Builder builder = RewriteMappings.create();

        final FindNodesByParentResult vHostsResults = doGetChildren( MAPPING_ROOT_NODE );

        vHostsResults.getNodeIds().stream().
            forEach( nodeId -> {
                final Node vHostNode = nodeService.getById( nodeId );
                builder.add( RewriteMappingSerializer.fromNode( vHostNode ) );
            } );

        return builder.build();
    }

    public void store( final RewriteMapping rewriteMapping )
    {
        final NodePath mappingNodePath = createContextNodePath( rewriteMapping.getContextKey() );

        final Context context = setRepoContext();

        context.runWith( () -> doCreateOrUpdate( mappingNodePath, rewriteMapping ) );
    }

    @Override
    public void create( final RewriteContextKey rewriteContextKey )
    {
        setRepoContext().runWith( () -> doCreate( rewriteContextKey ) );
    }

    private void doCreate( final RewriteContextKey rewriteContextKey )
    {
        final NodePath mappingNodePath = createContextNodePath( rewriteContextKey );

        final Node existingNode = doGetContextNode( rewriteContextKey );

        if ( existingNode != null )
        {
            throw new RewriteContextExistsException( rewriteContextKey );
        }

        doCreateContextNode( mappingNodePath, RewriteMapping.create().
            contextKey( rewriteContextKey ).
            rewriteRules( RewriteRules.create().build() ).
            build() );

        this.nodeService.refresh( RefreshMode.ALL );
    }

    @Override
    public void delete( final RewriteContextKey rewriteContextKey )
    {
        setRepoContext().runWith( () -> doDelete( rewriteContextKey ) );
    }

    @Override
    public void createRule( final CreateRuleParams params )
    {
        setRepoContext().runWith( () -> doCreateRule( params ) );
    }

    @Override
    public void deleteRule( final DeleteRuleParams params )
    {
        setRepoContext().runWith( () -> doDeleteRule( params ) );
    }

    private void doDeleteRule( final DeleteRuleParams params )
    {
        final RewriteContextKey contextKey = params.getContextKey();
        final Node contextNode = getExistingContextNode( contextKey );

        final RewriteMapping rewriteMapping = RewriteMappingSerializer.fromNode( contextNode );

        final RewriteRules.Builder newRules = RewriteRules.create();
        for ( final RewriteRule rule : rewriteMapping.getRewriteRules() )
        {
            if ( !rule.getFrom().equals( params.getFrom() ) )
            {
                newRules.addRule( rule );
            }
        }

        final RewriteMapping newMapping = RewriteMapping.create().
            contextKey( contextKey ).rewriteRules( newRules.build() ).
            build();

        doUpdateContextNode( contextNode, newMapping );
    }

    private void doCreateRule( final CreateRuleParams params )
    {
        final RewriteContextKey contextKey = params.getContextKey();

        final Node contextNode = getExistingContextNode( contextKey );

        final RewriteMapping rewriteMapping = RewriteMappingSerializer.fromNode( contextNode );

        final RewriteRule rule = RewriteRule.create().
            from( params.getFrom() ).
            target( params.getTarget() ).
            type( RedirectType.valueOf( params.getType() ) ).
            build();

        final RewriteMapping newMapping = RewriteMapping.create().
            contextKey( contextKey ).rewriteRules( RewriteRules.from( rewriteMapping.getRewriteRules() ).
            addRule( rule, params.getInsertStrategy().equalsIgnoreCase( "First" ) ).
            build() ).
            build();

        doUpdateContextNode( contextNode, newMapping );
    }

    private Node getExistingContextNode( final RewriteContextKey contextKey )
    {
        final Node existingNode = doGetContextNode( contextKey );

        if ( existingNode == null )
        {
            throw new RewriteContextNotFoundException( contextKey );
        }
        return existingNode;
    }

    private NodeIds doDelete( final RewriteContextKey rewriteContextKey )
    {
        final NodeIds nodeIds = this.nodeService.deleteByPath( createContextNodePath( rewriteContextKey ) );

        System.out.println( "Deleting nodes with id" + nodeIds );

        this.nodeService.refresh( RefreshMode.ALL );
        return nodeIds;
    }

    private Node doCreateOrUpdate( final NodePath nodePath, final RewriteMapping rewriteMapping )
    {
        final Node existing = this.nodeService.getByPath( nodePath );

        if ( existing == null )
        {
            return doCreateContextNode( nodePath, rewriteMapping );
        }
        else
        {
            return doUpdateContextNode( existing, rewriteMapping );
        }
    }

    private Node doCreateContextNode( final NodePath nodePath, final RewriteMapping rewriteMapping )
    {
        final PropertyTree data = RewriteMappingSerializer.toCreateNodeData( rewriteMapping );

        return this.nodeService.create( CreateNodeParams.create().
            name( nodePath.getName() ).
            parent( nodePath.getParentPath() ).
            data( data ).
            build() );
    }

    private Node doUpdateContextNode( final Node existing, final RewriteMapping rewriteMapping )
    {
        return this.nodeService.update( UpdateNodeParams.create().
            id( existing.id() ).
            path( existing.path() ).
            editor( RewriteMappingSerializer.toUpdateNodeData( rewriteMapping ) ).
            build() );
    }

    private Node doGetContextNode( final RewriteContextKey contextKey )
    {
        return this.nodeService.getByPath( createContextNodePath( contextKey ) );
    }

    private NodePath createContextNodePath( final RewriteContextKey contextKey )
    {
        return NodePath.create( MAPPING_ROOT_NODE, RewriteContextName.from( contextKey ).getName() ).build();
    }

    private Context setRepoContext()
    {
        return ContextBuilder.from( ContextAccessor.current() ).
            repositoryId( RewriteMappingRepoInitializer.REPO_ID ).
            branch( BRANCH ).
            build();
    }

    private FindNodesByParentResult doGetChildren( final NodePath parentPath )
    {
        //TODO: Ah, what the hell, fetch 10_000 and pretend its a good way of doing it for now
        return this.nodeService.findByParent( FindNodesByParentParams.create().
            parentPath( parentPath ).
            size( 10_000 ).
            build() );
    }

}
