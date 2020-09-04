package com.enonic.app.rewrite.requesttester;

import com.enonic.xp.web.vhost.VirtualHost;

public class IncomingRequest
{
    private final String requestUrl;

    private final VirtualHost matchingVHost;

    public IncomingRequest( final String requestUrl, final VirtualHost matchingVHost )
    {
        this.requestUrl = requestUrl;
        this.matchingVHost = matchingVHost;
    }

    public String getRequestUrl()
    {
        return requestUrl;
    }

    public VirtualHost getMatchingVHost()
    {
        return matchingVHost;
    }
}
