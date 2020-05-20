package com.enonic.app.rewrite;

public class CreateRuleParams
{
    private String contextKey;

    private Integer order;

    private String source;

    private String target;

    private String type;

    public String getContextKey()
    {
        return contextKey;
    }

    public void setContextKey( final String contextKey )
    {
        this.contextKey = contextKey;
    }

    public Integer getOrder()
    {
        return order;
    }

    public void setOrder( final Integer order )
    {
        this.order = order;
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
