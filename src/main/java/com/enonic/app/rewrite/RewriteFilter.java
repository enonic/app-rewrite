package com.enonic.app.rewrite;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.context.ContextResolver;
import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.app.rewrite.domain.RedirectExternal;
import com.enonic.app.rewrite.domain.RedirectTarget;
import com.enonic.app.rewrite.engine.RewriteEngine;
import com.enonic.app.rewrite.provider.RewriteRulesProvider;
import com.enonic.app.rewrite.provider.RewriteRulesProviderFactory;
import com.enonic.xp.annotation.Order;
import com.enonic.xp.web.filter.OncePerRequestFilter;

@Component(immediate = true, service = Filter.class)
@Order(100)
@WebFilter("/*")
public class RewriteFilter
    extends OncePerRequestFilter
{
    private RewriteFilterConfig config;

    private Patterns excludePatterns;

    private Patterns includePatterns;

    private RewriteEngine rewriteEngine;

    private RewriteRulesProviderFactory rewriteRulesProviderFactory;

    public final static Logger LOG = LoggerFactory.getLogger( RewriteFilter.class );

    @Activate
    public void activate()
    {
        System.out.println( "Activating RewriteFilter" );
        this.excludePatterns = new Patterns( config.excludePatterns() );
        final RewriteRulesProvider rewriteRulesProvider = this.rewriteRulesProviderFactory.get( this.config );
        this.rewriteEngine = new RewriteEngine( rewriteRulesProvider.provide() );
    }

    @Override
    protected void doHandle( final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain )
        throws Exception
    {
        LOG.info( "Handling in RewriteFilter" );
        LOG.debug( "Im in debug-mode" );

        final boolean responseCommitted = doRewriteURL( req, res );
        if ( !responseCommitted )
        {
            chain.doFilter( req, res );
        }
    }

    private boolean doExclude( final RequestWrapper request )
    {
        return this.excludePatterns.anyMatch( request.getRequestURI() );
    }

    private boolean doInclude( final RequestWrapper request )
    {
        //return this.includePatterns.anyMatch( request.getRequestURI() );
        return true;
    }

    private boolean doRewriteURL( HttpServletRequest hsRequest, HttpServletResponse hsResponse )
        throws Exception
    {
        LOG.info( "Checking if URL is target be rewritten" );

        if ( !this.config.enabled() )
        {
            return false;
        }

        final RequestWrapper wrappedRequest = new RequestWrapper( hsRequest );
        wrappedRequest.setContextPath( ContextResolver.resolve( hsRequest ) );

        if ( !doInclude( wrappedRequest ) || doExclude( wrappedRequest ) )
        {
            LOG.debug( "Skipped: " + hsRequest.getRequestURI() );
            return false;
        }
        final Redirect redirect = rewriteEngine.process( hsRequest );

        if ( redirect == null )
        {
            LOG.debug( "No matching rules: " + hsRequest.getRequestURI() );
            return false;
        }

        final RedirectTarget redirectTarget = redirect.getRedirectTarget();
        LOG.debug( "Changed from: " + hsRequest.getRequestURI() + " target: " + redirectTarget );

        final int httpCode = redirect.getType().getHttpCode();

        if ( redirect.getRedirectTarget() instanceof RedirectExternal )
        {
            hsResponse.setStatus( httpCode );
            hsResponse.sendRedirect( redirect.getRedirectTarget().getTargetPath() );
        }
        else
        {
            hsResponse.setStatus( httpCode );
            hsResponse.setHeader( "Location", redirect.getRedirectTarget().getTargetPath() );
        }
        return true;
    }

    @Reference
    public void setConfig( final RewriteFilterConfig config )
    {
        this.config = config;
    }

    @Reference
    public void setRewriteRulesProviderFactory( final RewriteRulesProviderFactory rewriteRulesProviderFactory )
    {
        this.rewriteRulesProviderFactory = rewriteRulesProviderFactory;
    }
}
