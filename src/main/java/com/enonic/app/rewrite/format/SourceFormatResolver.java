package com.enonic.app.rewrite.format;

public class SourceFormatResolver
{
    public static SourceFormat resolve( final String fileName )
    {
        final String extension = getFileExtension( fileName );

        if ( "csv".equals( extension ) )
        {
            return SourceFormat.CSV;
        }

        return SourceFormat.APACHE_REWRITE;
    }

    private static String getFileExtension( final String filename )
    {
        int lastIndexOf = filename.lastIndexOf( "." );
        if ( lastIndexOf == -1 )
        {
            return ""; // empty extension
        }
        return filename.substring( lastIndexOf + 1 ).toLowerCase();
    }
}
