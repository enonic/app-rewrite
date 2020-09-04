package com.enonic.app.rewrite.provider.repo;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.node.Node;

class ContextSerializer
{
    // What is context? Context is a node with a RewriteContext-properties

    static RewriteContextKey fromNode( final Node node )
    {
        return new RewriteContextKey( node.name().toString() );
    }

    static PropertyTree toCreateNodeData( final RewriteContextKey rule )
    {
        final PropertyTree propertyTree = new PropertyTree();

        return propertyTree;
    }

}
