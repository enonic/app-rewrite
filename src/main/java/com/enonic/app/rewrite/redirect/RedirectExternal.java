package com.enonic.app.rewrite.redirect;

import java.net.URI;

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
        return "RedirectExternal{ uri='" + uri + "'}";
    }

}

