package com.enonic.app.rewrite.requesttester;


import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.xp.web.vhost.VirtualHost;

public class RedirectTestResult
{
    private VirtualHost virtualHost;

    private RedirectMatch match;

    public RedirectTestResult( final VirtualHost virtualHost, final RedirectMatch match )
    {
        this.virtualHost = virtualHost;
        this.match = match;
    }

    public VirtualHost getVirtualHost()
    {
        return virtualHost;
    }

    public RedirectMatch getMatch()
    {
        return match;
    }

    @Override
    public String toString()
    {
        return "RedirectTestResult{" + "virtualHost=" + virtualHost + ", match=" + match;
    }
}

