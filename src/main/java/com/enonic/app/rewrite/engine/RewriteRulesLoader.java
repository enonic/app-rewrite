package com.enonic.app.rewrite.engine;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;

class RewriteRulesLoader
{
    static Map<RewriteContextKey, RulePatterns> load( final RewriteMapping mapping )
    {
        final Map<RewriteContextKey, RulePatterns> rewriteMap = Maps.newHashMap();

        for ( final RewriteContextKey contextKey : mapping.getRewriteRulesMap().keySet() )
        {
            final RulePatterns.Builder patternsBuilder = RulePatterns.create();

            for ( final RewriteRule rule : mapping.getRewriteRulesMap().get( contextKey ) )
            {
                final RulePattern pattern = RulePattern.create().
                    target( rule.getTarget() ).
                    order( rule.getOrder() ).
                    type( rule.getType() ).
                    pattern( rule.getFrom() ).
                    build();
                patternsBuilder.rule( pattern );
            }

            rewriteMap.put( contextKey, patternsBuilder.build() );
        }

        return rewriteMap;
    }

}
