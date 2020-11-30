package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class UpdateRuleParams
{
    private String ruleId;

    private RewriteContextKey contextKey;

    private String insertStrategy;

    private String source;

    private String target;

    private String type;

    private Integer position;

    public String getRuleId()
    {
        return ruleId;
    }

    public void setRuleId( final String ruleId )
    {
        this.ruleId = ruleId;
    }

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

    public String getSource()
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

    public Integer getPosition()
    {
        return position;
    }

    public void setPosition( final Integer position )
    {
        this.position = position;
    }
}
