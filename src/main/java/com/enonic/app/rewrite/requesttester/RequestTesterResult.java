package com.enonic.app.rewrite.requesttester;

import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.xp.web.vhost.VirtualHost;

public class RequestTesterResult
{

    private final VirtualHost virtualHost;

    private final Redirect redirect;

    public RequestTesterResult( final VirtualHost virtualHost, final Redirect redirect )
    {
        this.virtualHost = virtualHost;
        this.redirect = redirect;
    }

    public VirtualHost getVirtualHost()
    {
        return virtualHost;
    }

    public Redirect getRedirect()
    {
        return redirect;
    }
}
