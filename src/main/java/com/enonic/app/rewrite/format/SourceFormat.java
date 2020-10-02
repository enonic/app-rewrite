package com.enonic.app.rewrite.format;

public enum SourceFormat
{
    APACHE_REWRITE, CSV;

    public static SourceFormat get( final String formatString )
    {
        if ( formatString == null || formatString.isBlank() )
        {
            throw new IllegalArgumentException( "Cannot get SourceFormat of empty string" );
        }

        return SourceFormat.valueOf( formatString.toUpperCase() );
    }
}
