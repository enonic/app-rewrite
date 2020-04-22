package com.enonic.app.rewrite.provider.repo;

import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
import com.enonic.xp.context.Context;
import com.enonic.xp.context.ContextAccessor;
import com.enonic.xp.context.ContextBuilder;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.FindNodesByParentParams;
import com.enonic.xp.node.FindNodesByParentResult;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.repository.RepositoryService;

import static com.enonic.app.rewrite.provider.repo.RewriteMappingRepoInitializer.BRANCH;

public class RewriteRepoMappingProvider
    implements RewriteMappingProvider
{
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

        RewriteMappingRepoInitializer.create().
            repositoryService( repositoryService ).
            nodeService( nodeService ).
            setIndexService( indexService ).
            build().
            initialize();
    }


    @Override
    public RewriteMapping getAll()
    {
        return setRepoContext().callWith( this::doGetMappings );
    }

    private Node getOrCreateContext( final RewriteContextKey contextKey )
    {
        final Node existing = this.doGetContextNode( contextKey );
        if ( existing != null )
        {
            return existing;
        }

        return this.nodeService.create( CreateNodeParams.create().
            parent( MAPPING_ROOT_NODE ).
            name( RewriteContextName.from( contextKey ).getName() ).
            data( ContextSerializer.toCreateNodeData( contextKey ) ).
            build() );
    }

    @Override
    public void store( final RewriteContextKey contextKey, final RewriteRule rule )
    {
        final Context context = setRepoContext();

        context.runWith( () -> doStore( contextKey, rule ) );
    }

    private void doStore( final RewriteContextKey contextKey, final RewriteRule rule )
    {
        final Node contextNode = getOrCreateContext( contextKey );

        this.nodeService.create( CreateNodeParams.create().
            data( RewriteRuleSerializer.toCreateNodeData( rule ) ).
            name( RuleNodeName.from( rule ).getName() ).
            parent( contextNode.path() ).
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

    private RewriteMapping doGetMappings()
    {
        final RewriteMapping.Builder builder = RewriteMapping.create();

        final FindNodesByParentResult vHostsResults = doGetChildren( MAPPING_ROOT_NODE );

        vHostsResults.getNodeIds().stream().
            forEach( nodeId -> {
                final Node vHostNode = nodeService.getById( nodeId );
                final RewriteContextKey contextKey = ContextSerializer.fromNode( vHostNode );
                System.out.println( "ContextKey: " + contextKey );

                builder.add( contextKey, createRewriteRules( vHostNode ) );
            } );

        return builder.build();
    }

    private RewriteRules createRewriteRules( final Node vHostNode )
    {
        final FindNodesByParentResult result = doGetChildren( vHostNode.path() );

        final RewriteRules.Builder builder = RewriteRules.create();

        result.getNodeIds().forEach( nodeId -> {
            final RewriteRule rewriteRule = createRewriteRule( nodeId );
            builder.addRule( rewriteRule );
        } );

        return builder.build();
    }

    private RewriteRule createRewriteRule( final NodeId nodeId )
    {
        final Node ruleNode = this.nodeService.getById( nodeId );

        if ( ruleNode == null )
        {
            return null;
        }

        return RewriteRuleSerializer.fromNode( ruleNode );
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
