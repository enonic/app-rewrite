package com.enonic.app.rewrite.engine.processor;

import com.enonic.app.rewrite.domain.RewriteContext;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;

public class RewriteProcessor
{
    private final RulePatterns rulePatterns;

    private RewriteProcessor( final RulePatterns rulePatterns )
    {
        this.rulePatterns = rulePatterns;
    }

    public static RewriteProcessor from( final RewriteEngineConfig config )
    {
        final RulePatterns.Builder builder = RulePatterns.create();

        for ( final RewriteContext context : config.getRewriteRulesMap().keySet() )
        {
            int order = 0;
            for ( final RewriteRule rule : config.getRewriteRulesMap().get( context ) )
            {
                builder.rule( RulePattern.create().
                    target( rule.getTarget() ).
                    priority( order++ ).
                    type( rule.getType() ).
                    pattern( rule.getFrom() ).
                    context( context.context() ).
                    build() );
            }
        }

        return new RewriteProcessor( builder.build() );
    }

    public String match( final String value )
    {
        for ( final RulePattern rulePattern : this.rulePatterns )
        {
            final String match = rulePattern.match( value );
            if ( match != null )
            {
                return match;
            }
        }

        return null;
    }

}
