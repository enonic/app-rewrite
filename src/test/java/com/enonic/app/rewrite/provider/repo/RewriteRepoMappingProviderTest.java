package com.enonic.app.rewrite.provider.repo;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.index.IndexService;
import com.enonic.xp.node.FindNodesByParentParams;
import com.enonic.xp.node.FindNodesByParentResult;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodeIds;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.repository.RepositoryService;

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
        this.nodeService = Mockito.mock( NodeService.class );
        this.repositoryService = Mockito.mock( RepositoryService.class );
    }

    @Test
    void testMapping()
    {
        final NodeId vhostNodeId = NodeId.from( "vhost1" );
        final NodeIds vhostNodeIds = NodeIds.from( vhostNodeId );

        final NodeId ruleNodeId = NodeId.from( "rule1" );
        final NodeIds rulesNodeIds = NodeIds.from( ruleNodeId );

        Mockito.when( this.nodeService.findByParent( Mockito.isA( FindNodesByParentParams.class ) ) ).
            thenReturn( FindNodesByParentResult.create().nodeIds( vhostNodeIds ).build() ).
            thenReturn( FindNodesByParentResult.create().nodeIds( rulesNodeIds ).build() );

        final PropertyTree data = new PropertyTree();
        mockGetNodeById( vhostNodeId, data );

        final RewriteRule rule = RewriteRule.create().
            from( "/fromPath" ).
            target( "/targetPath" ).
            order( 0 ).
            type( RedirectType.MOVED_PERMANENTLY ).
            build();
        mockGetNodeById( ruleNodeId, RewriteRuleSerializer.toCreateNodeData( rule ) );

        final RewriteRepoMappingProvider provider =
            new RewriteRepoMappingProvider( this.repositoryService, this.indexService, this.nodeService );

        final RewriteMapping rewriteMapping = provider.getAll();
        final Map<RewriteContextKey, RewriteRules> rewriteRulesMap = rewriteMapping.getRewriteRulesMap();
        assertEquals( rewriteRulesMap.keySet().size(), 1 );
        final RewriteRules rewriteRules = rewriteRulesMap.get( new RewriteContextKey( vhostNodeId.toString() ) );
        assertNotNull( rewriteRules );
        assertEquals( rewriteRules.size(), 1 );
    }

    private void mockGetNodeById( final NodeId nodeId, final PropertyTree data )
    {
        Mockito.when( this.nodeService.getById( nodeId ) ).thenReturn( Node.create( nodeId ).
            parentPath( RewriteRepoMappingProvider.MAPPING_ROOT_NODE ).
            name( nodeId.toString() ).
            data( data ).
            build() );
    }
}