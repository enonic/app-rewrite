package com.enonic.app.rewrite.context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SiteContextResolver
{
    private final static Pattern sitePattern = Pattern.compile( "^/site/([^/]*)/([^/]*)/(.*)" );

    static String resolve( String path )
    {
        final Matcher matcher = sitePattern.matcher( path );
        if ( matcher.matches() )
        {
            return matcher.group( 3 );
        }
        return path;
    }
}
