package com.enonic.app.rewrite;

public class RequestTesterParams
{
    private String host;

    private String requestPath;

    private String rewriteContext;

    public String getHost()
    {
        return host;
    }

    public void setHost( final String host )
    {
        this.host = host;
    }

    public String getRequestPath()
    {
        return requestPath;
    }

    public void setRequestPath( final String requestPath )
    {
        this.requestPath = requestPath;
    }

    public String getRewriteContext()
    {
        return rewriteContext;
    }

    public void setRewriteContext( final String rewriteContext )
    {
        this.rewriteContext = rewriteContext;
    }
}
