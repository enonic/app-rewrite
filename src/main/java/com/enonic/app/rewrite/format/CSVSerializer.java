package com.enonic.app.rewrite.format;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.google.common.base.Strings;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;

public class CSVSerializer
{
    public static final RedirectType DEFAULT_REDIRECT_TYPE = RedirectType.MOVED_PERMANENTLY;

    static SourceReadResult read( final BufferedReader reader )
    {
        try
        {
            return doParseFile( reader );
        }
        catch ( IOException e )
        {
            return SourceReadResult.create().build();
        }
    }

    private static SourceReadResult doParseFile( final BufferedReader reader )
        throws IOException
    {

        final SourceReadResult.Builder builder = SourceReadResult.create();
        final RewriteRules.Builder rulesBuilder = RewriteRules.create();

        final CSVParser parser = CSVParser.parse( reader, CSVFormat.DEFAULT.withFirstRecordAsHeader() );
        for ( CSVRecord csvRecord : parser )
        {
            try
            {
                rulesBuilder.addRule( doParseRecord( csvRecord ) );
                builder.addNew();
            }
            catch ( Exception e )
            {
                builder.addFailed();
            }
        }

        return builder.
            rules( rulesBuilder.build() ).
            build();
    }

    private static RewriteRule doParseRecord( final CSVRecord csvRecord )
    {
        final String from = csvRecord.get( "From" );
        final String target = csvRecord.get( "Target" );
        final String type = csvRecord.get( "Type" );

        return RewriteRule.create().
            from( from ).
            target( target ).
            type( parseType( type ) ).
            build();
    }

    private static RedirectType parseType( final String value )
    {
        if ( Strings.isNullOrEmpty( value ) )
        {
            return DEFAULT_REDIRECT_TYPE;
        }

        try
        {
            final int code = Integer.parseInt( value );
            return RedirectType.valueOf( code );
        }
        catch ( NumberFormatException e )
        {
            return DEFAULT_REDIRECT_TYPE;
        }
    }

}
