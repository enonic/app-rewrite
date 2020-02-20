package com.enonic.app.rewrite.domain;

public class SimpleRewriteContext
    implements RewriteContext
{
    private final RewriteContextKey key;

    private final String sourceContext;

    private final String targetContext;

    public SimpleRewriteContext( final String key, final String sourceContext, final String targetContext )
    {
        this.key = new RewriteContextKey( key );
        this.sourceContext = sourceContext;
        this.targetContext = targetContext;
    }

    @Override
    public RewriteContextKey getKey()
    {
        return this.key;
    }

    @Override
    public String getSourceContext()
    {
        return this.sourceContext;
    }

    @Override
    public String getTargetContext()
    {
        return this.targetContext;
    }
}