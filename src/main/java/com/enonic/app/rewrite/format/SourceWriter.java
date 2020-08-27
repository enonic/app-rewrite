package com.enonic.app.rewrite.format;

import com.enonic.app.rewrite.rewrite.RewriteRules;

public class SourceWriter
{

    public static String serialize( final RewriteRules rules, final SourceFormat format )
    {
        switch ( format )
        {
            case APACHE_REWRITE:
            {
                return ApacheRewriteSerializer.serialize( rules );
            }

            case CSV:
            {
                return CSVSerializer.serialize( rules );
            }

            default:
                return CSVSerializer.serialize( rules );

        }

    }

}
