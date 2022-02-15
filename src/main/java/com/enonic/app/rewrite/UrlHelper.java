package com.enonic.app.rewrite;

public class UrlHelper
{
    public static String createUrl( final String context, final String path )
    {
        return ( "/".equals( context ) ? "" : context ) + "/" + normalize( path );
    }

    private static String normalize( final String value )
    {
        return value.startsWith( "/" ) ? value.substring( 1 ) : value;
    }
}
