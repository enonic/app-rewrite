package com.enonic.app.rewrite.filter;

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

import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.context.ContextResolver;
import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectTarget;
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

    private RewriteService rewriteService;

    private final static Logger LOG = LoggerFactory.getLogger( RewriteFilter.class );

    @Activate
    public void activate()
    {
        this.excludePatterns = new Patterns( config.excludePatterns() );
    }

    @Override
    protected void doHandle( final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain )
        throws Exception
    {
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
        final RedirectMatch match = rewriteService.process( hsRequest );

        if ( match == null )
        {
            LOG.debug( "No matching rules: " + hsRequest.getRequestURI() );
            return false;
        }

        final Redirect redirect = match.getRedirect();
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
    public void setRewriteService( final RewriteService rewriteService )
    {
        this.rewriteService = rewriteService;
    }
}
