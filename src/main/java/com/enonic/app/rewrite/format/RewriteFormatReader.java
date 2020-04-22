package com.enonic.app.rewrite.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.rewrite.RewriteRule;

public class RewriteFormatReader
{
    private static final Logger LOG = LoggerFactory.getLogger( RewriteFormatReader.class );

    public static RewriteRule read( final String value, final int order )
    {
        RewriteRule rule = null;

        try
        {
            rule = ApacheRewriteFormatReader.read( value, order );
            if ( rule != null )
            {
                return rule;
            }
        }
        catch ( Exception e )
        {
            LOG.error( "Cannot read rewrite-line [%s]", value );
        }

        LOG.warn( "Cannot understand rewrite-rule %s, ignoring", value );
        return null;

    }


}
