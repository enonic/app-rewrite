package com.enonic.app.rewrite.provider.repo;

import java.util.HashMap;
import java.util.Map;

import org.mockito.Mockito;

import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.DeleteNodeParams;
import com.enonic.xp.node.DeleteNodeResult;
import com.enonic.xp.node.EditableNode;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodeName;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.node.UpdateNodeParams;

/**
 * Stateful in-memory NodeService stub for unit tests. Backed by Mockito with
 * stubbed answers for the small subset of NodeService methods exercised by the
 * tests in this package: create, getById, getByPath, update, delete, refresh.
 * Any other call returns Mockito's default (null / false / empty), which is
 * fine for these tests but will surface as a NullPointerException downstream
 * if a test grows to depend on additional methods — extend this stub then.
 */
final class NodeServiceMock
{
    private final Map<NodeId, Node> nodesById = new HashMap<>();

    private final Map<NodePath, Node> nodesByPath = new HashMap<>();

    private final NodeService delegate;

    NodeServiceMock()
    {
        this.delegate = Mockito.mock( NodeService.class );

        Mockito.when( delegate.create( Mockito.any( CreateNodeParams.class ) ) )
            .thenAnswer( invocation -> doCreate( invocation.getArgument( 0 ) ) );

        Mockito.when( delegate.update( Mockito.any( UpdateNodeParams.class ) ) )
            .thenAnswer( invocation -> doUpdate( invocation.getArgument( 0 ) ) );

        Mockito.when( delegate.getByPath( Mockito.any( NodePath.class ) ) )
            .thenAnswer( invocation -> nodesByPath.get( invocation.<NodePath>getArgument( 0 ) ) );

        Mockito.when( delegate.getById( Mockito.any( NodeId.class ) ) )
            .thenAnswer( invocation -> nodesById.get( invocation.<NodeId>getArgument( 0 ) ) );

        Mockito.when( delegate.delete( Mockito.any( DeleteNodeParams.class ) ) )
            .thenAnswer( invocation -> doDelete( invocation.getArgument( 0 ) ) );
    }

    NodeService asNodeService()
    {
        return delegate;
    }

    private Node doCreate( final CreateNodeParams params )
    {
        final NodeId id = params.getNodeId() != null ? params.getNodeId() : NodeId.from( System.nanoTime() );
        final Node node = Node.create()
            .id( id )
            .name( NodeName.from( params.getName() ) )
            .parentPath( params.getParent() )
            .childOrder( params.getChildOrder() )
            .data( params.getData() )
            .build();
        nodesById.put( node.id(), node );
        nodesByPath.put( node.path(), node );
        return node;
    }

    private Node doUpdate( final UpdateNodeParams params )
    {
        final Node existing = nodesById.get( params.getId() );
        if ( existing == null )
        {
            return null;
        }
        final EditableNode editable = new EditableNode( existing );
        params.getEditor().edit( editable );
        final Node updated = Node.create( editable.build() ).permissions( existing.getPermissions() ).build();
        nodesById.put( updated.id(), updated );
        nodesByPath.put( updated.path(), updated );
        return updated;
    }

    private DeleteNodeResult doDelete( final DeleteNodeParams params )
    {
        final Node existing = nodesByPath.get( params.getNodePath() );
        if ( existing != null )
        {
            nodesById.remove( existing.id() );
            nodesByPath.remove( existing.path() );
        }
        return Mockito.mock( DeleteNodeResult.class );
    }
}
