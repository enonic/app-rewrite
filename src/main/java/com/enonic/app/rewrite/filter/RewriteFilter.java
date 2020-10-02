package com.enonic.app.rewrite.filter;

import java.nio.charset.StandardCharsets;

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
import com.enonic.app.rewrite.URLPathEncoder;
import com.enonic.app.rewrite.context.ContextResolver;
import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectTarget;
import com.enonic.xp.annotation.Order;
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
            LOG.error( "Rewritefilter failed", e );
        }

        if ( !responseCommitted )
        {
            chain.doFilter( req, res );
        }
    }

    private boolean doExclude( final RequestWrapper request )
    {
        return this.excludePatternMatcher.match( request.getRequestURI() );
    }

    private boolean doInclude( final RequestWrapper request )
    {
        return this.includePatternMatcher.match( request.getRequestURI() );
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
            hsResponse.setHeader( "Location",
                                  URLPathEncoder.encode( redirect.getRedirectTarget().getTargetPath(), StandardCharsets.UTF_8 ) );
        }
        return true;
    }

}
