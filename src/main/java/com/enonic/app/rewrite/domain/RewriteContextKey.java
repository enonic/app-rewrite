package com.enonic.app.rewrite.domain;

import java.util.Objects;

public class RewriteContextKey
{
    private final String value;

    public RewriteContextKey( final String value )
    {
        this.value = value;
    }

    public static RewriteContextKey from( final String value )
    {
        return new RewriteContextKey( value );
    }

    @Override
    public String toString()
    {
        return value;
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
        final RewriteContextKey that = (RewriteContextKey) o;
        return Objects.equals( value, that.value );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( value );
    }
}


