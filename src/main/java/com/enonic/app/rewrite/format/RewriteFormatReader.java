package com.enonic.app.rewrite.format;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import com.enonic.app.rewrite.io.ReadRulesResult;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;

public class RewriteFormatReader
{
    private static final Logger LOG = LoggerFactory.getLogger( RewriteFormatReader.class );


    public static RewriteReaderResult read( final String value )
    {
        RewriteRule rule = null;

        try
        {
            rule = ApacheRewriteFormatReader.read( value );
            if ( rule != null )
            {
                return new RewriteReaderResult( rule, RewriteReaderState.OK );
            }
        }
        catch ( Exception e )
        {
            return new RewriteReaderResult( null, RewriteReaderState.FAILED );
        }

        return new RewriteReaderResult( null, RewriteReaderState.UNKNOWN_FORMAT );
    }


    public static ReadRulesResult read( final ByteSource byteSource )
    {
        final RewriteRules.Builder builder = RewriteRules.create();

        final ReadRulesResult.Builder readResult = ReadRulesResult.create();

        try
        {
            byteSource.asCharSource( Charsets.UTF_8 ).forEachLine( line -> {
                final RewriteReaderResult result = RewriteFormatReader.read( line );
                final RewriteReaderState state = result.getState();
                if ( state.equals( RewriteReaderState.OK ) )
                {
                    builder.addRule( result.getRule() );
                    readResult.addNew();
                }
                else if ( state.equals( RewriteReaderState.FAILED ) )
                {
                    readResult.addFailed();
                    LOG.warn( "Could not resolve rules from line {}", line );
                }
            } );
        }
        catch ( IOException e )
        {
            LOG.error( "Cannot read rewrite file" );
        }

        return readResult.rules( builder.build() ).
            build();
    }


}
