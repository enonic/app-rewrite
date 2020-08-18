package com.enonic.app.rewrite.format;

import java.io.BufferedReader;
import java.io.IOException;

public class SourceReader
{
    public static SourceReadResult read( final BufferedReader reader, final SourceFormat format )
        throws IOException
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
