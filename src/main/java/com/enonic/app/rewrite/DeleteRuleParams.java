package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class DeleteRuleParams
{
    private String from;

    private RewriteContextKey contextKey;

    public void setContextKey( final String contextKey )
    {
        this.contextKey = RewriteContextKey.from( contextKey );
    }

    public void setFrom( final String from )
    {
        this.from = from;
    }

    public String getFrom()
    {
        return from;
    }

    public RewriteContextKey getContextKey()
    {
        return contextKey;
    }
}
