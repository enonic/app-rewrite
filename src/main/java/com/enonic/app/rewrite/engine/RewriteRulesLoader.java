package com.enonic.app.rewrite.engine;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;

class RewriteRulesLoader
{
    static Map<RewriteContextKey, RulePatterns> load( final RewriteMappings mappings )
    {
        final Map<RewriteContextKey, RulePatterns> rewriteMap = Maps.newHashMap();

        for ( final RewriteMapping rewriteMapping : mappings )
        {
            final RulePatterns.Builder patternsBuilder = RulePatterns.create();

            for ( final RewriteRule rule : rewriteMapping )
            {
                final RulePattern pattern = RulePattern.create().
                    target( rule.getTarget() ).
                    order( rule.getOrder() ).
                    type( rule.getType() ).
                    pattern( rule.getFrom() ).
                    build();
                patternsBuilder.rule( pattern );
            }

            rewriteMap.put( rewriteMapping.getContextKey(), patternsBuilder.build() );
        }

        return rewriteMap;
    }
}
