package com.enonic.app.rewrite.format;

import java.io.File;

public class SourceFormatResolver
{
    SourceFormat resolve( final File file )
    {
        final String extension = getFileExtension( file );

        if ( extension.equals( ".conf" ) )
        {
            return SourceFormat.APACHE_REWRITE;
        }

        if ( extension.equals( ".csv" ) )
        {
            return SourceFormat.CVS;
        }

        return SourceFormat.CVS;
    }


    private String getFileExtension( File file )
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
