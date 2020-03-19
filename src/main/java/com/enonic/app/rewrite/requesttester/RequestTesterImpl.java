package com.enonic.app.rewrite.requesttester;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.base.Strings;

import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

@Component(immediate = true)
public class RequestTesterImpl
    implements RequestTester
{
    private VirtualHostConfig config;

    private RewriteService rewriteService;

    @Override
    public RequestTesterResult testRequest( final String requestURL )
    {
        if ( Strings.isNullOrEmpty( requestURL ) )
        {
            return new RequestTesterResult( null, null );
        }

        final RewriteTesterRequest req = new RewriteTesterRequest( requestURL );
        getAndSetVHost( req );

        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( req );
        final Redirect redirect = this.rewriteService.process( req );

        return new RequestTesterResult( virtualHost, redirect );
    }

    private void getAndSetVHost( final RewriteTesterRequest req )
    {
        final VirtualHostMapping virtualHostMapping = this.config.getMappings().resolve( req );
        if ( virtualHostMapping != null )
        {
            VirtualHostHelper.setVirtualHost( req, virtualHostMapping );
        }
    }

    @Reference
    public void setConfig( final VirtualHostConfig config )
    {
        this.config = config;
    }

    @Reference
    public void setRewriteService( final RewriteService rewriteService )
    {
        this.rewriteService = rewriteService;
    }
}
