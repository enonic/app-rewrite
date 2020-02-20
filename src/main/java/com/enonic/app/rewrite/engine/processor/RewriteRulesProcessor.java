package com.enonic.app.rewrite.engine.processor;

import java.nio.file.Paths;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.domain.RewriteContext;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;

public class RewriteRulesProcessor
{


    private final Map<RewriteContextKey, RulePatterns> rewriteMap;

    private final static Logger LOG = LoggerFactory.getLogger( RewriteRulesProcessor.class );

    public RewriteRulesProcessor( final Map<RewriteContextKey, RulePatterns> rewriteMap )
    {
        this.rewriteMap = rewriteMap;
    }

    public static RewriteRulesProcessor from( final RewriteEngineConfig config )
    {
        final Map<RewriteContextKey, RulePatterns> rewriteMap = Maps.newHashMap();

        final RulePatterns.Builder patternsBuilder = RulePatterns.create();

        for ( final RewriteContextKey contextKey : config.getRewriteRulesMap().keySet() )
        {
            int order = 0;
            for ( final RewriteRule rule : config.getRewriteRulesMap().get( contextKey ) )
            {
                final RulePattern pattern = RulePattern.create().
                    target( rule.getTarget() ).
                    priority( order++ ).
                    type( rule.getType() ).
                    pattern( rule.getFrom() ).
                    build();

                LOG.info( "Adding rule-pattern: {}", pattern );

                patternsBuilder.rule( pattern );
            }

            rewriteMap.put( contextKey, patternsBuilder.build() );
        }

        return new RewriteRulesProcessor( rewriteMap );
    }

    public String match( final RewriteContext rewriteContext, final String requestPath )
    {
        if ( requestPath == null )
        {
            return null;
        }

        final RulePatterns rulePatterns = this.rewriteMap.get( rewriteContext.getKey() );

        if ( rulePatterns == null )
        {
            return null;
        }

        for ( final RulePattern rulePattern : rulePatterns )
        {
            final String match = rulePattern.match( removeContextPrefix( rewriteContext, requestPath ) );
            if ( match != null )
            {
                return joinWithContext( rewriteContext, match );
            }
        }

        return null;
    }

    private String removeContextPrefix( final RewriteContext context, final String path )
    {
        return path.replaceFirst( context.getTargetContext(), "" );
    }

    private String joinWithContext( final RewriteContext context, final String match )
    {
        final String sourceContext = context.getSourceContext();
        return Paths.get( sourceContext, match ).toString();

        //return sourceContext.endsWith( "/" ) ? context.getSourceContext() + match : context.getSourceContext() + "/" + match;
    }


}
