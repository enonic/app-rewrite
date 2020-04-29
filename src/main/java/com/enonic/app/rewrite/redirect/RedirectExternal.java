package com.enonic.app.rewrite.redirect;

import java.net.URI;
import java.util.Objects;

public class RedirectExternal
    implements RedirectTarget
{
    private final URI uri;

    private RedirectExternal( final URI uri )
    {
        this.uri = uri;
    }

    public static RedirectExternal from( final String value )
    {
        return new RedirectExternal( URI.create( value ) );
    }

    @Override
    public String getTargetPath()
    {
        return this.uri.toString();
    }

    @Override
    public String toString()
    {
        return "RedirectExternal{" + "uri=" + uri + '}';
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
        final RedirectExternal that = (RedirectExternal) o;
        return Objects.equals( uri, that.uri );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( uri );
    }
}

