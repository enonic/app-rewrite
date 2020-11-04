package com.enonic.app.rewrite.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;

class RewriteRulesLoader
{
    static Map<RewriteContextKey, RulePatterns> load( final Collection<RewriteMapping> mappings )
    {
        final Map<RewriteContextKey, RulePatterns> rewriteMap = new HashMap<>();

        for ( final RewriteMapping rewriteMapping : mappings )
        {
            final RulePatterns.Builder patternsBuilder = RulePatterns.create();

            for ( final RewriteRule rule : rewriteMapping.getRewriteRules().getRuleList() )
            {
                final RulePattern pattern = RulePattern.create().
                    target( rule.getTarget().path() ).
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
