package com.enonic.app.rewrite.provider.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.repository.RepositoryService;

import static com.enonic.app.rewrite.provider.repo.RewriteMappingRepoInitializer.REPO_ID;
import static com.enonic.app.rewrite.provider.repo.RewriteRepoMappingProvider.MAPPING_ROOT_NODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RewriteRepoMappingProviderTest
{

    private IndexService indexService;

    private NodeService nodeService;

    private RepositoryService repositoryService;

    @BeforeEach
    void setUp()
    {
        this.indexService = Mockito.mock( IndexService.class );
        this.nodeService = new NodeServiceMock();
        this.repositoryService = Mockito.mock( RepositoryService.class );

        Mockito.when( this.indexService.isMaster() ).thenReturn( true );
        Mockito.when( this.repositoryService.isInitialized( REPO_ID ) ).thenReturn( true );

        this.nodeService.create( CreateNodeParams.create().
            parent( MAPPING_ROOT_NODE.getParentPath() ).
            name( MAPPING_ROOT_NODE.getName() ).
            build() );

    }

    @Test
    void testCreateRewriteMapping()
    {

        final RewriteRepoMappingProvider service =
            new RewriteRepoMappingProvider( this.repositoryService, this.indexService, this.nodeService );

        final RewriteMapping rewriteMapping = RewriteMapping.create().
            contextKey( new RewriteContextKey( "myVHost" ) ).
            rewriteRules( RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/original" ).
                    target( "/target" ).
                    order( 0 ).
                    type( RedirectType.MOVED_PERMANENTLY ).
                    build() ).
                build() ).
            build();

        service.store( rewriteMapping );

        final Node storedNode = this.nodeService.getByPath( NodePath.create( MAPPING_ROOT_NODE, "myVHost" ).build() );
        assertNotNull( storedNode );
        final RewriteMapping storedRewriteMapping = RewriteMappingSerializer.fromNode( storedNode );
        assertEquals( storedRewriteMapping, rewriteMapping );
    }
}