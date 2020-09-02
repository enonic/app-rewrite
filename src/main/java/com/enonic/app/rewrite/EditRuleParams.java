package com.enonic.app.rewrite;

import com.enonic.app.rewrite.rewrite.RewriteContextKey;

public class EditRuleParams
{
    private RewriteContextKey contextKey;

    private String newPattern;

    private String pattern;

    private String substitution;

    private String type;

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

}
