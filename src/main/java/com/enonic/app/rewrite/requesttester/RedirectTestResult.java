package com.enonic.app.rewrite.requesttester;


import com.enonic.app.rewrite.redirect.RedirectMatch;

public class RedirectTestResult
{
    private IncomingRequest incomingRequest;

    private RedirectMatch match;

    public RedirectTestResult( final IncomingRequest incomingRequest, final RedirectMatch match )
    {
        this.incomingRequest = incomingRequest;
        this.match = match;
    }

    public IncomingRequest getIncomingRequest()
    {
        return incomingRequest;
    }

    public RedirectMatch redirectMatch()
    {
        return match;
    }

    @Override
    public String toString()
    {
        return "RedirectTestResult{" + "incomingRequest=" + incomingRequest + ", match=" + match + '}';
    }
}

