package com.enonic.app.rewrite.domain;

public class SimpleRewriteContext implements RewriteContext
{
    private final String context;

    public SimpleRewriteContext( final String context )
    {
        this.context = context;
    }

    @Override
    public String context()
    {
        return null;
    }
}
