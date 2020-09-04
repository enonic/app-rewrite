package com.enonic.app.rewrite.domain;

import java.util.Objects;

public class RewriteTarget
{
    private final String path;

    private final boolean isExternal;

    private RewriteTarget( final String path )
    {
        this.path = path;
        this.isExternal = path.startsWith( "http" );
    }

    public static RewriteTarget from( final String targetString )
    {
        return new RewriteTarget( targetString );
    }

    public String path()
    {
        return path;
    }

    public boolean isExternal()
    {
        return isExternal;
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
        final RewriteTarget that = (RewriteTarget) o;
        return isExternal == that.isExternal && Objects.equals( path, that.path );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( path, isExternal );
    }
}


