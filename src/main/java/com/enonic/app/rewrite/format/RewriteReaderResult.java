package com.enonic.app.rewrite.format;

import com.enonic.app.rewrite.rewrite.RewriteRule;

public class RewriteReaderResult
{
    private final RewriteRule rule;

    private final RewriteReaderState state;

    public RewriteReaderResult( final RewriteRule rule, final RewriteReaderState state )
    {
        this.rule = rule;
        this.state = state;
    }

    public RewriteRule getRule()
    {
        return rule;
    }

    public RewriteReaderState getState()
    {
        return state;
    }
}
