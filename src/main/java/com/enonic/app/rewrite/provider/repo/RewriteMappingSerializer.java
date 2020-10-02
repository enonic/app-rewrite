package com.enonic.app.rewrite.provider.repo;

import java.util.ArrayList;
import java.util.List;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.xp.data.PropertySet;
import com.enonic.xp.data.PropertyTree;
import com.enonic.xp.node.Node;
import com.enonic.xp.node.NodeEditor;

public class RewriteMappingSerializer
{
    private static final String RULE_IDENTIFIER = "ruleId";

    private static final String RULE_FROM_KEY = "from";

    private static final String RULE_TARGET_KEY = "to";

    private static final String RULE_ORDER_KEY = "order";

    private static final String RULE_TYPE_KEY = "type";

    private static final String CONTEXT_KEY = "rewriteContextKey";

    private static final String RULES_KEY = "rewriteRules";

    static RewriteMapping fromNode( final Node node )
    {
        final PropertyTree data = node.data();

        return RewriteMapping.create().
            contextKey( new RewriteContextKey( data.getString( CONTEXT_KEY ) ) ).
            rewriteRules( createRewriteRules( node ) ).
            build();
    }

    private static RewriteRules createRewriteRules( final Node node )
    {
        final RewriteRules.Builder builder = RewriteRules.create();

        final Iterable<PropertySet> ruleSets = node.data().getSets( RULES_KEY );

        if ( ruleSets != null )
        {
            ruleSets.forEach( ( ruleSet ) -> builder.addRule( RewriteRule.create().
                ruleId( ruleSet.getString( RULE_IDENTIFIER ) ).
                from( ruleSet.getString( RULE_FROM_KEY ) ).
                target( ruleSet.getString( RULE_TARGET_KEY ) ).
                type( RedirectType.valueOf( ruleSet.getString( RULE_TYPE_KEY ) ) ).
                build() ) );
        }

        return builder.build();
    }

    static PropertyTree toCreateNodeData( final RewriteMapping rewriteMapping )
    {
        final PropertyTree propertyTree = new PropertyTree();
        final PropertySet data = propertyTree.getRoot();

        if ( rewriteMapping.getRewriteRules() != null )
        {
            data.addSets( RULES_KEY, createRules( rewriteMapping.getRewriteRules() ) );
        }

        data.setString( CONTEXT_KEY, rewriteMapping.getContextKey().toString() );
        return propertyTree;
    }

    static NodeEditor toUpdateNodeData( final RewriteMapping rewriteMapping )
    {
        return toBeEdited -> toBeEdited.data = toCreateNodeData( rewriteMapping );
    }

    private static PropertySet[] createRules( final RewriteRules rewriteRules )
    {
        final List<PropertySet> setList = new ArrayList<>();

        rewriteRules.forEach( rule -> {
            final PropertySet ruleData = new PropertySet();
            ruleData.addString( RULE_IDENTIFIER, rule.getRuleId() );
            ruleData.addString( RULE_FROM_KEY, rule.getFrom() );
            ruleData.addString( RULE_TARGET_KEY, rule.getTarget().path() );
            ruleData.addString( RULE_TYPE_KEY, rule.getType().name() );
            setList.add( ruleData );
        } );

        return setList.toArray( new PropertySet[setList.size()] );
    }

    private static Double toDouble( final Integer value )
    {
        return value != null ? value.doubleValue() : null;
    }


}
