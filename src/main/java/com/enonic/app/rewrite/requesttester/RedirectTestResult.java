package com.enonic.app.rewrite.requesttester;


import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.xp.web.vhost.VirtualHost;

public class RedirectTestResult
{
    private final String requestUrl;

    private final VirtualHost matchingVHost;

    private RedirectMatch match;

    public RedirectTestResult( final String requestUrl, final VirtualHost matchingVHost, final RedirectMatch match )
    {
        this.requestUrl = requestUrl;
        this.matchingVHost = matchingVHost;
        this.match = match;
    }

    public String getRequestUrl()
    {
        return requestUrl;
    }

    public VirtualHost getMatchingVHost()
    {
        return matchingVHost;
    }

    public RedirectMatch redirectMatch()
    {
        return match;
    }

    @Override
    public String toString()
    {
        return "RedirectTestResult{ requestUrl=" + requestUrl + ", matchingVHost=" + matchingVHost + ", match=" + match + "}";
    }
}

