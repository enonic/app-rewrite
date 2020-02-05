package com.enonic.app.rewrite;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.RewrittenUrl;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;

import com.enonic.app.rewrite.provider.RewriteRulesProvider;
import com.enonic.app.rewrite.provider.RewriteRulesProviderFactory;

@Component(immediate = true)
public class RewriteEngineImpl
    implements RewriteEngine
{
    private UrlRewriter urlRewriter;

    public final static Logger LOG = LoggerFactory.getLogger( RewriteEngine.class );

    private RewriteFilterConfig config;

    private RewriteRulesProviderFactory rewriteRulesProviderFactory;

    @Activate
    public void activate()
    {
        final RewriteRulesProvider rewriteRulesProvider = this.rewriteRulesProviderFactory.get( this.config );
        final Conf conf = rewriteRulesProvider.get();
        this.urlRewriter = new UrlRewriter( conf );
    }

    public String process( final HttpServletRequest request, final HttpServletResponse response )
    {
        try
        {
            final RewrittenUrl rewrittenUrl = urlRewriter.processRequest( request, response );
            return rewrittenUrl != null ? rewrittenUrl.getTarget() : null;
        }
        catch ( IOException | ServletException | InvocationTargetException e )
        {
            e.printStackTrace();
        }

        return null;
    }

    public boolean process( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain )
    {
        try
        {
            return urlRewriter.processRequest( request, response, filterChain );
        }
        catch ( IOException | ServletException e )
        {
            e.printStackTrace();
        }

        return false;
    }



    @Reference
    @SuppressWarnings("unused")
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
