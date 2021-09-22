package com.enonic.app.rewrite.engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.domain.RewriteContext;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteTarget;
import com.enonic.app.rewrite.domain.RewriteVirtualHostContext;
import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectInternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

public class RewriteEngine
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteEngine.class );

    private Map<RewriteContextKey, RulePatterns> rewriteMap;

    public RedirectMatch process( final HttpServletRequest request )
    {
        return process( request, null );
    }

    public RedirectMatch process( final HttpServletRequest request, final ExtRulePattern extRulePattern )
    {
        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( request );

        if ( virtualHost != null )
        {
            return match( new RewriteVirtualHostContext( virtualHost ), request, extRulePattern );
        }

        return null;
    }

    public RewriteRulesLoadResult load( final Collection<RewriteMapping> mappings )
    {
        this.rewriteMap = RewriteRulesLoader.load( mappings );

        final RewriteRulesLoadResult.Builder result = RewriteRulesLoadResult.create();

        this.rewriteMap.keySet().forEach( ( k ) -> result.add( k, this.rewriteMap.get( k ).size() ) );

        return result.build();
    }

    private RedirectMatch match( final RewriteContext rewriteContext, final HttpServletRequest request,
                                 final ExtRulePattern extRulePattern )
    {
        if ( request.getRequestURI() == null )
        {
            return null;
        }

        if ( this.rewriteMap == null )
        {
            return null;
        }

        final RulePatterns rulePatterns = this.rewriteMap.get( rewriteContext.getKey() );

        if ( rulePatterns == null )
        {
            return null;
        }

        if ( extRulePattern == null )
        {
            return match( rewriteContext, request, rulePatterns.getRules() );
        }

        final List<RulePattern> rules = new ArrayList<>( rulePatterns.getRules() );
        rules.add( extRulePattern.getPosition(), extRulePattern.getRulePattern() );

        return match( rewriteContext, request, rules );
    }

    private RedirectMatch match( final RewriteContext rewriteContext, final HttpServletRequest request,
                                 final List<RulePattern> rulePatterns )
    {
        final String urlInContext = removeContextPrefix( rewriteContext, request.getRequestURI() );

        for ( final RulePattern rulePattern : rulePatterns )
        {
            final Matcher matcher = rulePattern.getPattern().matcher( urlInContext );
            if ( !matcher.matches() )
            {
                continue;
            }

            LOG.debug( "Redirect-match found: [{}] : [{}]", request.getRequestURI(), rulePattern.getTarget() );

            return new RedirectMatch( createRedirect( rewriteContext, rulePattern, matcher, request.getQueryString() ),
                                      rulePattern.getOrder() );
        }

        return null;
    }

    private Redirect createRedirect( final RewriteContext rewriteContext, final RulePattern rulePattern, final Matcher matcher,
                                     final String queryString )
    {
        final RewriteTarget target = rulePattern.getTarget();

        String replacedTarget = matcher.replaceFirst( target.path() );

        if ( queryString != null && !replacedTarget.contains( "?" ) )
        {
            replacedTarget = replacedTarget + "?" + queryString;
        }

        if ( target.isExternal() )
        {
            return new Redirect( RedirectExternal.from( replacedTarget ), rulePattern.getType() );
        }

        return new Redirect( RedirectInternal.from( rewriteContext, replacedTarget ), rulePattern.getType() );
    }

    private String removeContextPrefix( final RewriteContext context, final String path )
    {

        final String replaced = path.replaceFirst( context.getTargetContext(), "" );

        return replaced.isEmpty() ? "/" : replaced;
    }
}
