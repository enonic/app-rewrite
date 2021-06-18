package com.enonic.app.rewrite;

public class UrlHelper
{
    public static String createUrl( final String context, final String path )
    {
        return normalize( context ) + "/" + normalize( path );
    }

    private static String normalize( final String value )
    {
        return value.startsWith( "/" ) ? value.substring( 1 ) : value;
    }
}
