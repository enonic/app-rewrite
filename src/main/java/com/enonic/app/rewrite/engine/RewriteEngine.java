package com.enonic.app.rewrite.engine;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.domain.RedirectMatch;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteVirtualHostContext;
import com.enonic.app.rewrite.engine.processor.RewriteProcessor;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

public class RewriteEngine
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteEngine.class );

    private RewriteProcessor rewriteProcessor;


    public RewriteEngine( final RewriteMapping rewriteMapping )
    {
        this.rewriteProcessor = RewriteProcessor.from( rewriteMapping );
    }

    public RedirectMatch process( final HttpServletRequest request )
    {
        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( request );

        if ( virtualHost != null )
        {
            return this.rewriteProcessor.match( new RewriteVirtualHostContext( virtualHost ), request.getRequestURI() );
        }

        return null;
    }

}
