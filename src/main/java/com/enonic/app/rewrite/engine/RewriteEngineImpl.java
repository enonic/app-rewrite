package com.enonic.app.rewrite.engine;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.engine.processor.RewriteProcessor;

@Component(immediate = true)
public class RewriteEngineImpl
    implements RewriteEngine
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteEngine.class );

    private RewriteEngineConfig config;

    private RewriteProcessor rewriteProcessor;


    @Activate
    public void activate()
    {
        this.rewriteProcessor = RewriteProcessor.from( config );
    }

    @Override
    public String process( final HttpServletRequest request )
    {
        LOG.info( "Processing request: " + request.getRequestURI() );

        if ( !request.getRequestURI().equals( "/redirect" ) )
        {
            return "/redirected";
        }

        return null;
    }


    private String doProcess( final HttpServletRequest request )
    {
        return this.rewriteProcessor.match( request.getRequestURI() );
    }

}
