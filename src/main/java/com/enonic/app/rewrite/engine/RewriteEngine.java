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
            return match( new RewriteVirtualHostContext( virtualHost ), request.getRequestURI(), extRulePattern );
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

    private RedirectMatch match( final RewriteContext rewriteContext, final String requestPath, final ExtRulePattern extRulePattern )
    {
        if ( requestPath == null )
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
            return match( rewriteContext, requestPath, rulePatterns.getRules() );
        }

        final List<RulePattern> rules = new ArrayList<>( rulePatterns.getRules() );
        rules.add( extRulePattern.getPosition(), extRulePattern.getRulePattern() );

        return match( rewriteContext, requestPath, rules );
    }

    private RedirectMatch match( final RewriteContext rewriteContext, final String requestPath, final List<RulePattern> rulePatterns )
    {
        final String urlInContext = removeContextPrefix( rewriteContext, requestPath );

        for ( final RulePattern rulePattern : rulePatterns )
        {
            final Matcher matcher = rulePattern.getPattern().matcher( urlInContext );
            if ( !matcher.matches() )
            {
                continue;
            }

            LOG.debug( "Redirect-match found: [{}] : [{}]", requestPath, rulePattern.getTarget() );

            return new RedirectMatch( createRedirect( rewriteContext, rulePattern, matcher ), rulePattern.getOrder() );
        }

        return null;
    }

    private Redirect createRedirect( final RewriteContext rewriteContext, final RulePattern rulePattern, final Matcher matcher )
    {
        final RewriteTarget target = rulePattern.getTarget();

        final String replacedTarget = matcher.replaceFirst( target.path() );

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
