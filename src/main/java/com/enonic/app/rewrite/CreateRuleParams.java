package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class CreateRuleParams
{
    private RewriteContextKey contextKey;

    private String insertStrategy;

    private String source;

    private String target;

    private String type;

    public RewriteContextKey getContextKey()
    {
        return contextKey;
    }

    public void setContextKey( final String contextKey )
    {
        this.contextKey = RewriteContextKey.from( contextKey );
    }

    public String getInsertStrategy()
    {
        return insertStrategy;
    }

    public void setInsertStrategy( final String insertStrategy )
    {
        this.insertStrategy = insertStrategy;
    }

    public String getFrom()
    {
        return source;
    }

    public void setSource( final String source )
    {
        this.source = source;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget( final String target )
    {
        this.target = target;
    }

    public String getType()
    {
        return type;
    }

    public void setType( final String type )
    {
        this.type = type;
    }
}
