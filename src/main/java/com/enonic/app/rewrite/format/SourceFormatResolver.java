package com.enonic.app.rewrite.format;

import java.io.File;

public class SourceFormatResolver
{
    public static SourceFormat resolve( final File file )
    {
        final String extension = getFileExtension( file );
        if ( extension.equals( "conf" ) )
        {
            return SourceFormat.APACHE_REWRITE;
        }
        else if ( extension.equals( "csv" ) )
        {
            return SourceFormat.CVS;
        }

        return SourceFormat.CVS;
    }

    private static String getFileExtension( File file )
    {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf( "." );
        if ( lastIndexOf == -1 )
        {
            return ""; // empty extension
        }
        return name.substring( lastIndexOf + 1 );
    }
}
