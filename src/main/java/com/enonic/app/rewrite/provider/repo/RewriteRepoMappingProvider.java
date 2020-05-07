package com.enonic.app.rewrite.provider.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.xp.context.Context;
import com.enonic.xp.context.ContextAccessor;
import com.enonic.xp.context.ContextBuilder;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.FindNodesByParentParams;
import com.enonic.xp.node.FindNodesByParentResult;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
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
    public RewriteMappings getRewriteMappings()
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
