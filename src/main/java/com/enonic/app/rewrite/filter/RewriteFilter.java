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
import com.enonic.xp.trace.Trace;
import com.enonic.xp.trace.Tracer;
import com.enonic.xp.web.filter.OncePerRequestFilter;

@Component(enabled = false, service = Filter.class)
@Order(100)
@WebFilter("/*")
public class RewriteFilter
    extends OncePerRequestFilter
{
    private static final Logger LOG = LoggerFactory.getLogger( RewriteFilter.class );

    private RewriteFilterConfig config;

    private PatternMatcher excludePatternMatcher;

    private PatternMatcher includePatternMatcher;

    private RewriteService rewriteService;

    @Activate
    public RewriteFilter( @Reference final RewriteFilterConfig config, @Reference final RewriteService rewriteService )
    {
        this.config = config;
        this.rewriteService = rewriteService;

        this.excludePatternMatcher = new PatternMatcher( config.excludePattern() );
        this.includePatternMatcher = new PatternMatcher( config.includePattern() );
    }

    @Override
    protected void doHandle( final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain )
        throws Exception
    {
        boolean responseCommitted = false;
        try
        {
            responseCommitted = doRewriteURL( req, res );
        }
        catch ( Exception e )
        {
            LOG.error( "RewriteFilter failed", e );
        }

        if ( !responseCommitted )
        {
            chain.doFilter( req, res );
        }
    }

    private boolean doExclude( final String requestURI )
    {
        return this.excludePatternMatcher.match( requestURI );
    }

    private boolean doInclude( final String requestURI )
    {
        return this.includePatternMatcher.match( requestURI );
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

        final String requestURI = wrappedRequest.getRequestURI();

        if ( !doInclude( requestURI ) || doExclude( requestURI ) )
        {
            LOG.debug( "Skipped: {}", requestURI );
            return false;
        }

        RedirectMatch match;

        final Trace trace = Tracer.newTrace( "rewriteFilter" );
        if ( trace == null )
        {
            match = rewriteService.process( wrappedRequest );
        }
        else
        {
            match = Tracer.trace( trace, () -> rewriteService.process( wrappedRequest ) );

            trace.put( "requestURI", requestURI );
            if ( match != null )
            {
                trace.put( "statusCode", match.getRedirect().getType().getHttpCode() );
                trace.put( "targetPath", match.getRedirect().getRedirectTarget().getTargetPath() );
            }
        }

        if ( match == null )
        {
            LOG.debug( "No matching rules: {}", requestURI );
            return false;
        }

        final Redirect redirect = match.getRedirect();
        final RedirectTarget redirectTarget = redirect.getRedirectTarget();
        LOG.debug( "Changed from: {} target: {}", requestURI, redirectTarget );

        final int httpCode = redirect.getType().getHttpCode();

        if ( redirectTarget instanceof RedirectExternal )
        {
            hsResponse.setStatus( httpCode );
            hsResponse.sendRedirect( redirectTarget.getTargetPath() );
        }
        else
        {
            hsResponse.setStatus( httpCode );
            hsResponse.setHeader( "Location", redirectTarget.getTargetPath() );
        }
        return true;
    }

}
