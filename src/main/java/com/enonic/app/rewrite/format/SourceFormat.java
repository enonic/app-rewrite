package com.enonic.app.rewrite.format;

import com.google.common.base.Strings;

public enum SourceFormat
{
    APACHE_REWRITE, CSV;

    public static SourceFormat get( final String formatString )
    {
        if ( Strings.isNullOrEmpty( formatString ) )
        {
            throw new IllegalArgumentException( "Cannot get SourceFormat of empty string" );
        }

        return SourceFormat.valueOf( formatString.toUpperCase() );
    }
}
