package com.enonic.app.rewrite.engine.processor;

import java.util.Map;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.app.rewrite.domain.RedirectExternal;
import com.enonic.app.rewrite.domain.RedirectInternal;
import com.enonic.app.rewrite.domain.RewriteContext;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteTarget;
import com.enonic.app.rewrite.domain.RewriteMapping;

public class RewriteProcessor
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteProcessor.class );

    private final Map<RewriteContextKey, RulePatterns> rewriteMap;

    public RewriteProcessor( final Map<RewriteContextKey, RulePatterns> rewriteMap )
    {
        this.rewriteMap = rewriteMap;
    }

    public static RewriteProcessor from( final RewriteMapping config )
    {
        final Map<RewriteContextKey, RulePatterns> rewriteMap = Maps.newHashMap();

        final RulePatterns.Builder patternsBuilder = RulePatterns.create();

        for ( final RewriteContextKey contextKey : config.getRewriteRulesMap().keySet() )
        {
            int priority = 0;

            for ( final RewriteRule rule : config.getRewriteRulesMap().get( contextKey ) )
            {
                final RulePattern pattern = RulePattern.create().
                    target( rule.getTarget() ).
                    priority( priority++ ).
                    type( rule.getType() ).
                    pattern( rule.getFrom() ).
                    build();

                LOG.info( "Adding rule-pattern for context(): {}", contextKey, pattern );

                patternsBuilder.rule( pattern );
            }

            rewriteMap.put( contextKey, patternsBuilder.build() );
        }

        return new RewriteProcessor( rewriteMap );
    }

    public Redirect match( final RewriteContext rewriteContext, final String requestPath )
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

        final String urlInContext = removeContextPrefix( rewriteContext, requestPath );

        for ( final RulePattern rulePattern : rulePatterns )
        {
            final Matcher matcher = rulePattern.getPattern().matcher( urlInContext );
            if ( !matcher.matches() )
            {
                continue;
            }
            return createRedirect( rewriteContext, rulePattern, matcher );
        }

        return null;
    }

    private Redirect createRedirect( final RewriteContext rewriteContext, final RulePattern rulePattern, final Matcher matcher )
    {
        final RewriteTarget target = rulePattern.getTarget();

        if ( target.isExternal() )
        {
            return new Redirect( RedirectExternal.from( target.path() ), rulePattern.getType() );
        }

        final String replacedTarget = matcher.replaceFirst( rulePattern.getTarget().path() );
        return new Redirect( RedirectInternal.from( rewriteContext, replacedTarget ), rulePattern.getType() );
    }

    private String removeContextPrefix( final RewriteContext context, final String path )
    {
        return path.replaceFirst( context.getTargetContext(), "" );
    }

}
