package com.enonic.app.rewrite.provider.format;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.domain.RewriteRule;

public class RewriteFormatReader
{
    private static final Logger LOG = LoggerFactory.getLogger( RewriteFormatReader.class );

    public static RewriteRule read( final String value )
    {
        RewriteRule rule = null;

        try
        {
            rule = ApacheRewriteFormatReader.read( value );
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
