package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class DeleteRuleParams
{
    private String ruleId;

    private RewriteContextKey contextKey;

    public void setContextKey( final String contextKey )
    {
        this.contextKey = RewriteContextKey.from( contextKey );
    }

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
}
