package com.enonic.app.rewrite.format;

import java.io.File;

public class SourceFormatResolver
{
    public static SourceFormat resolve( final String fileName )
    {
        final String extension = getFileExtension( fileName );
        if ( extension.equals( "conf" ) )
        {
            return SourceFormat.APACHE_REWRITE;
        }
        else if ( extension.equals( "csv" ) )
        {
            return SourceFormat.CSV;
        }

        return SourceFormat.CSV;
    }

    private static String getFileExtension( final String filename )
    {
        int lastIndexOf = filename.lastIndexOf( "." );
        if ( lastIndexOf == -1 )
        {
            return ""; // empty extension
        }
        return filename.substring( lastIndexOf + 1 );
    }
}
