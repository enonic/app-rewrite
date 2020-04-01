package com.enonic.app.rewrite.repo;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.repo.model.VHost;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.FindNodesByParentParams;
import com.enonic.xp.node.FindNodesByParentResult;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.repository.Repository;
import com.enonic.xp.repository.RepositoryService;

@Component(immediate = true)
public class RewriteRepoMappingProvider
    implements RewriteMappingProvider
{
    private RepositoryService repositoryService;

    private IndexService indexService;

    private NodeService nodeService;

    private final static NodePath MAPPING_ROOT_NODE = NodePath.create( "/vhosts" ).build();

    @Activate
    public void activate( final Map<String, String> map )
    {
        RewriteMappingRepoInitializer.create().
            repositoryService( repositoryService ).
            setIndexService( indexService ).
            build().
            initialize();
    }


    @Override
    public RewriteMapping get()
    {
        final RewriteMapping.Builder builder = RewriteMapping.create();

        final Repository repo = this.repositoryService.get( RewriteMappingRepoInitializer.REPO_ID );

        //TODO: Ah, what the hell, fetch 10_000 and pretend its a good way of doing it for now
        final FindNodesByParentResult vHostsResults = this.nodeService.findByParent( FindNodesByParentParams.create().
            parentPath( MAPPING_ROOT_NODE ).
            size( 10_000 ).
            build() );

        vHostsResults.getNodeIds().stream().forEach( nodeId -> {
            final Node vHostNode = nodeService.getById( nodeId );
            builder.add( new RewriteContextKey( VHost.fromNode( vHostNode ).getName() ), createRewriteRules( nodeId ) );
        } );

        return builder.build();
    }

    private RewriteRules createRewriteRules( final NodeId nodeId )
    {
        return RewriteRules.create().build();
    }
}
