package com.enonic.app.rewrite.format;

import com.enonic.app.rewrite.domain.RewriteRule;

public class RewriteReaderResult
{
    private final RewriteRule rule;

    private final RewriteRuleReadResult state;

    public RewriteReaderResult( final RewriteRule rule, final RewriteRuleReadResult state )
    {
        this.rule = rule;
        this.state = state;
    }

    public RewriteRule getRule()
    {
        return rule;
    }

    public RewriteRuleReadResult getState()
    {
        return state;
    }
}
