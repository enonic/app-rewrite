package com.enonic.app.rewrite.format;

import java.io.BufferedReader;

public class SourceReader
{
    public static SourceReadResult read( final BufferedReader reader, final SourceFormat format )
    {
        switch ( format )
        {
            case APACHE_REWRITE:
            {
                return ApacheRewriteSerializer.read( reader );
            }

            case CVS:
            {
                return CSVSerializer.read( reader );
            }

            default:
                return SourceReadResult.create().build();

        }

    }

}
