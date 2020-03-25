package com.enonic.app.rewrite.requesttester;

import com.enonic.app.rewrite.domain.RedirectMatch;
import com.enonic.xp.web.vhost.VirtualHost;

public class RequestTesterResult
{

    private final VirtualHost virtualHost;

    private final RedirectMatch match;

    public RequestTesterResult( final VirtualHost virtualHost, final RedirectMatch match )
    {
        this.virtualHost = virtualHost;
        this.match = match;
    }

    public VirtualHost getVirtualHost()
    {
        return virtualHost;
    }

    public RedirectMatch getRedirectMatch()
    {
        return match;
    }
}
