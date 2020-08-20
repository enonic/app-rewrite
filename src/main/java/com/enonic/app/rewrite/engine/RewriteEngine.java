package com.enonic.app.rewrite.engine;

import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectInternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContext;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteTarget;
import com.enonic.app.rewrite.rewrite.RewriteVirtualHostContext;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

public class RewriteEngine
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteEngine.class );

    private Map<RewriteContextKey, RulePatterns> rewriteMap;

    public RedirectMatch process( final HttpServletRequest request )
    {
        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( request );

        if ( virtualHost != null )
        {
            return this.match( new RewriteVirtualHostContext( virtualHost ), request.getRequestURI() );
        }

        return null;
    }

    public RewriteRulesLoadResult load( final RewriteMappings mapping )
    {
        this.rewriteMap = RewriteRulesLoader.load( mapping );

        final RewriteRulesLoadResult.Builder result = RewriteRulesLoadResult.create();

        this.rewriteMap.keySet().forEach( ( k ) -> {
            result.add( k, this.rewriteMap.get( k ).size() );
        } );

        return result.build();
    }

    private RedirectMatch match( final RewriteContext rewriteContext, final String requestPath )
    {
        if ( requestPath == null )
        {
            return null;
        }

        if ( this.rewriteMap == null )
        {
            LOG.warn( "Rewrite-map not initialized" );
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
            return new RedirectMatch( createRedirect( rewriteContext, rulePattern, matcher ), rulePattern.getOrder() );
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
