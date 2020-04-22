package com.enonic.app.rewrite;

public class StoreRuleParams
{
    private String contextKey;

    private String from;

    private String target;

    private Integer order;

    public String getContextKey()
    {
        return contextKey;
    }

    public void setContextKey( final String contextKey )
    {
        this.contextKey = contextKey;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom( final String from )
    {
        this.from = from;
    }

    public String getTarget()
    {
        return target;
    }

    public void setTarget( final String target )
    {
        this.target = target;
    }

    public Integer getOrder()
    {
        return order;
    }

    public void setOrder( final Integer order )
    {
        this.order = order;
    }

    @Override
    public String toString()
    {
        return "StoreRuleParams{" + "contextKey='" + contextKey + '\'' + ", from='" + from + '\'' + ", target='" + target + '\'' +
            ", order=" + order + '}';
    }
}
