package com.enonic.app.rewrite.provider.repo;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.xp.data.PropertySet;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.node.Node;

class RewriteRuleSerializer
{

    public static final String FROM_KEY = "from";

    public static final String TARGET_KEY = "to";

    public static final String ORDER_KEY = "order";

    public static final String TYPE_KEY = "type";

    static RewriteRule fromNode( final Node node )
    {
        final PropertyTree data = node.data();

        return RewriteRule.create().
            from( data.getString( FROM_KEY ) ).
            target( data.getString( TARGET_KEY ) ).
            order( data.getDouble( ORDER_KEY ).intValue() ).
            type( RedirectType.valueOf( data.getString( TYPE_KEY ) ) ).
            build();
    }

    static PropertyTree toCreateNodeData( final RewriteRule rule )
    {
        final PropertyTree propertyTree = new PropertyTree();
        final PropertySet data = propertyTree.getRoot();

        data.addString( FROM_KEY, rule.getFrom() );
        data.addString( TARGET_KEY, rule.getTarget().path() );
        data.addDouble( ORDER_KEY, toDouble( rule.getOrder() ) );
        data.addString( TYPE_KEY, rule.getType().name() );

        return propertyTree;
    }

    private static Double toDouble( final Integer value )
    {
        return value != null ? value.doubleValue() : null;
    }


}
