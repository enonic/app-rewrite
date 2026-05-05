package com.enonic.app.rewrite.provider.repo;

import java.util.HashMap;
import java.util.Map;

import com.google.common.io.ByteSource;

import com.enonic.xp.branch.Branch;
import com.enonic.xp.node.ApplyNodePermissionsParams;
import com.enonic.xp.node.ApplyNodePermissionsResult;
import com.enonic.xp.node.ApplyVersionAttributesParams;
import com.enonic.xp.node.Attributes;
import com.enonic.xp.node.CommitNodeParams;
import com.enonic.xp.node.CreateNodeParams;
import com.enonic.xp.node.DeleteNodeParams;
import com.enonic.xp.node.DeleteNodeResult;
import com.enonic.xp.node.DuplicateNodeParams;
import com.enonic.xp.node.DuplicateNodeResult;
import com.enonic.xp.node.EditableNode;
import com.enonic.xp.node.FindNodesByMultiRepoQueryResult;
import com.enonic.xp.node.FindNodesByParentParams;
import com.enonic.xp.node.FindNodesByParentResult;
import com.enonic.xp.node.FindNodesByQueryResult;
import com.enonic.xp.node.GetActiveNodeVersionsParams;
import com.enonic.xp.node.GetActiveNodeVersionsResult;
import com.enonic.xp.node.GetNodeVersionsParams;
import com.enonic.xp.node.GetNodeVersionsResult;
import com.enonic.xp.node.ImportNodeParams;
import com.enonic.xp.node.ImportNodeResult;
import com.enonic.xp.node.MoveNodeParams;
import com.enonic.xp.node.MoveNodeResult;
import com.enonic.xp.node.MultiRepoNodeQuery;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeCommitEntry;
import com.enonic.xp.node.NodeCommitId;
import com.enonic.xp.node.NodeCommitQuery;
import com.enonic.xp.node.NodeCommitQueryResult;
import com.enonic.xp.node.NodeComparison;
import com.enonic.xp.node.NodeComparisons;
import com.enonic.xp.node.NodeId;
import com.enonic.xp.node.NodeIds;
import com.enonic.xp.node.NodeName;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodePaths;
import com.enonic.xp.node.NodeQuery;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.node.NodeStorageException;
import com.enonic.xp.node.NodeVersion;
import com.enonic.xp.node.NodeVersionId;
import com.enonic.xp.node.NodeVersionQuery;
import com.enonic.xp.node.NodeVersionQueryResult;
import com.enonic.xp.node.Nodes;
import com.enonic.xp.node.PatchNodeParams;
import com.enonic.xp.node.PatchNodeResult;
import com.enonic.xp.node.PushNodeParams;
import com.enonic.xp.node.PushNodesResult;
import com.enonic.xp.node.RefreshMode;
import com.enonic.xp.node.ResolveSyncWorkResult;
import com.enonic.xp.node.SortNodeParams;
import com.enonic.xp.node.SortNodeResult;
import com.enonic.xp.node.SyncWorkResolverParams;
import com.enonic.xp.node.UpdateNodeParams;
import com.enonic.xp.util.BinaryReference;

class NodeServiceMock
    implements NodeService
{
    private final Map<NodeId, Node> nodeIdMap = new HashMap<>();

    private final Map<NodePath, Node> nodePathMap = new HashMap<>();

    private final MockNodeTree<NodePath> nodeTree = new MockNodeTree<>( NodePath.ROOT );

    @Override
    public Node create( final CreateNodeParams params )
    {
        final Node createdNode = Node.create().
            id( params.getNodeId() != null ? params.getNodeId() : NodeId.from( System.nanoTime() ) ).
            name( NodeName.from( params.getName() ) ).
            parentPath( params.getParent() ).
            manualOrderValue( params.getManualOrderValue() ).
            childOrder( params.getChildOrder() ).
            data( params.getData() ).
            build();

        nodeIdMap.putIfAbsent( createdNode.id(), createdNode );
        nodePathMap.putIfAbsent( createdNode.path(), createdNode );

        final MockNodeTree<NodePath> nodePathTreeNode = this.nodeTree.find( createdNode.parentPath() );

        if ( nodePathTreeNode == null )
        {
            throw new NodeStorageException( "Cannot store node " + createdNode.path() + ", parent not found: " + createdNode.parentPath() );
        }

        nodePathTreeNode.addChild( createdNode.path() );

        return createdNode;
    }

    @Override
    public Node update( final UpdateNodeParams params )
    {
        final Node persistedNode = nodeIdMap.get( params.getId() );
        final EditableNode editableNode = new EditableNode( persistedNode );
        params.getEditor().edit( editableNode );

        final Node editedNode = editableNode.build();
        if ( editedNode.equals( persistedNode ) )
        {
            return persistedNode;
        }

        final Node updated = Node.create( editedNode ).
            permissions( persistedNode.getPermissions() ).
            build();

        nodeIdMap.put( updated.id(), updated );
        nodePathMap.put( updated.path(), updated );

        return updated;
    }

    @Override
    public DeleteNodeResult delete( final DeleteNodeParams params )
    {
        final NodePath path = params.getNodePath();
        final NodeId id = params.getNodeId();

        final Node toBeRemoved;
        if ( path != null )
        {
            toBeRemoved = this.nodePathMap.get( path );
        }
        else
        {
            toBeRemoved = this.nodeIdMap.get( id );
        }

        if ( toBeRemoved == null )
        {
            return DeleteNodeResult.create().build();
        }

        final MockNodeTree<NodePath> treeNode = nodeTree.find( toBeRemoved.path() );
        if ( treeNode != null && treeNode.getParent() != null )
        {
            treeNode.getParent().children.remove( treeNode );
        }

        this.nodePathMap.remove( toBeRemoved.path() );
        this.nodeIdMap.remove( toBeRemoved.id() );

        return DeleteNodeResult.create().
            add( new DeleteNodeResult.Result( toBeRemoved.id(), null ) ).
            build();
    }

    @Override
    public Node getByPath( final NodePath path )
    {
        return nodePathMap.get( path );
    }

    @Override
    public Node getById( final NodeId id )
    {
        return nodeIdMap.get( id );
    }

    @Override
    public Nodes getByIds( final NodeIds ids )
    {
        final Nodes.Builder builder = Nodes.create();
        ids.forEach( id -> builder.add( this.nodeIdMap.get( id ) ) );
        return builder.build();
    }

    @Override
    public void refresh( final RefreshMode refreshMode )
    {
        // no-op
    }

    @Override
    public boolean nodeExists( final NodeId nodeId )
    {
        return nodeIdMap.containsKey( nodeId );
    }

    @Override
    public boolean nodeExists( final NodePath nodePath )
    {
        return nodePathMap.containsKey( nodePath );
    }

    @Override
    public PatchNodeResult patch( final PatchNodeParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public MoveNodeResult move( final MoveNodeParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public PushNodesResult push( final PushNodeParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public Node getByIdAndVersionId( final NodeId id, final NodeVersionId versionId )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeVersion getVersion( final NodeId nodeId, final NodeVersionId nodeVersionId )
    {
        return null;
    }

    @Override
    public Nodes getByPaths( final NodePaths paths )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public DuplicateNodeResult duplicate( final DuplicateNodeParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public FindNodesByParentResult findByParent( final FindNodesByParentParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public FindNodesByQueryResult findByQuery( final NodeQuery nodeQuery )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public FindNodesByMultiRepoQueryResult findByQuery( final MultiRepoNodeQuery nodeQuery )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeComparison compare( final NodeId id, final Branch target )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeComparisons compare( final NodeIds ids, final Branch target )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public GetNodeVersionsResult getVersions( final GetNodeVersionsParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeVersionQueryResult findVersions( final NodeVersionQuery nodeVersionQuery )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeCommitQueryResult findCommits( final NodeCommitQuery nodeCommitQuery )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public GetActiveNodeVersionsResult getActiveVersions( final GetActiveNodeVersionsParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public SortNodeResult sort( final SortNodeParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public ResolveSyncWorkResult resolveSyncWork( final SyncWorkResolverParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public ApplyNodePermissionsResult applyPermissions( final ApplyNodePermissionsParams params )
    {
        return ApplyNodePermissionsResult.create().build();
    }

    @Override
    public ByteSource getBinary( final NodeId nodeId, final BinaryReference reference )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public ByteSource getBinary( final NodeId nodeId, final NodeVersionId nodeVersionId, final BinaryReference reference )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public ImportNodeResult importNode( final ImportNodeParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeCommitEntry commit( final CommitNodeParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeCommitEntry commit( final NodeCommitEntry nodeCommitEntry, final NodeIds nodeIds )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public NodeCommitEntry getCommit( final NodeCommitId nodeCommitId )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public boolean hasUnpublishedChildren( final NodeId parent, final Branch target )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }

    @Override
    public Attributes applyVersionAttributes( final ApplyVersionAttributesParams params )
    {
        throw new UnsupportedOperationException( "Not implemented in mock" );
    }
}
