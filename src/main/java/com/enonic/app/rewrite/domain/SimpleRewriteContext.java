package com.enonic.app.rewrite.domain;

import java.util.Objects;

public class SimpleRewriteContext
    implements RewriteContext
{
    private final String context;

    public SimpleRewriteContext( final String context )
    {
        this.context = context;
    }

    @Override
    public String context()
    {
        return context;
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final SimpleRewriteContext that = (SimpleRewriteContext) o;
        return Objects.equals( context, that.context );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( context );
    }
}