package com.enonic.app.rewrite.engine;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.app.rewrite.domain.RewriteVirtualHostContext;
import com.enonic.app.rewrite.engine.processor.RewriteProcessor;
import com.enonic.app.rewrite.engine.processor.RulePatterns;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

public class RewriteEngine
{
    private final static Logger LOG = LoggerFactory.getLogger( RewriteEngine.class );

    private RewriteProcessor rewriteProcessor;

    private final Map<String, RulePatterns> rewriteMap = Maps.newHashMap();

    public RewriteEngine( final RewriteEngineConfig rewriteEngineConfig )
    {
        this.rewriteProcessor = RewriteProcessor.from( rewriteEngineConfig );
    }

    public Redirect process( final HttpServletRequest request )
    {
        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( request );

        if ( virtualHost != null )
        {
            return this.rewriteProcessor.match( new RewriteVirtualHostContext( virtualHost ), request.getRequestURI() );
        }

        return null;
    }


}
