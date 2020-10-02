package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class EditRuleParams
{
    private RewriteContextKey contextKey;

    private String ruleId;

    private String newPattern;

    private String pattern;

    private String substitution;

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

    public String getNewPattern()
    {
        return newPattern;
    }

    public void setNewPattern( final String newPattern )
    {
        this.newPattern = newPattern;
    }

    public void setContextKey( final String contextKey )
    {
        this.contextKey = RewriteContextKey.from( contextKey );
    }

    public String getPattern()
    {
        return pattern;
    }

    public void setPattern( final String pattern )
    {
        this.pattern = pattern;
    }

    public String getSubstitution()
    {
        return substitution;
    }

    public void setSubstitution( final String substitution )
    {
        this.substitution = substitution;
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
