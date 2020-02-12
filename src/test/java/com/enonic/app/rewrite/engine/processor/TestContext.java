package com.enonic.app.rewrite.engine.processor;

import com.enonic.app.rewrite.domain.RewriteContext;

public class TestContext
    implements RewriteContext
{
    private final String context;

    public TestContext( final String context )
    {
        this.context = context;
    }

    @Override
    public String context()
    {
        return this.context;
    }
}
