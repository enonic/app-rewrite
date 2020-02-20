package com.enonic.app.rewrite.engine;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.engine.processor.RewriteProcessor;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

public class RewriteEngine
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteEngine.class );

    private RewriteEngineConfig rewriteEngineConfig;

    private RewriteProcessor rewriteProcessor;

    public RewriteEngine( final RewriteEngineConfig rewriteEngineConfig )
    {
        // do we need to keep the config?
        this.rewriteEngineConfig = rewriteEngineConfig;
        this.rewriteProcessor = RewriteProcessor.from( rewriteEngineConfig );
    }

    public String process( final HttpServletRequest request )
    {
        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( request );

        return this.rewriteProcessor.match( request.getRequestURI() );
    }

    private String doProcess( final HttpServletRequest request )
    {
        return this.rewriteProcessor.match( request.getRequestURI() );
    }

}
