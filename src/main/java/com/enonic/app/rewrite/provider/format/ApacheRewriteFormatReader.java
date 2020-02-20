package com.enonic.app.rewrite.provider.format;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteRule;

class ApacheRewriteFormatReader
{
    private final static Pattern apacheFormat = Pattern.compile( "^\\s*RewriteRule\\s+(\\S+)\\s+(\\S+)(\\s+\\[(.*)])?" );

    private static String REDIRECT_CODE_KEY = "R";

    private static RedirectType DEFAULT_REDIRECT_TYPE = RedirectType.MOVED_PERMANENTLY;

    static RewriteRule read( final String value )
    {
        if ( Strings.isNullOrEmpty( value ) )
        {
            return null;
        }

        final Matcher matcher = apacheFormat.matcher( value );

        if ( matcher.matches() )
        {
            final String from = clean( matcher.group( 1 ) );
            final String to = clean( matcher.group( 2 ) );
            final Map<String, String> flags = parseFlags( matcher.group( 4 ) );

            return RewriteRule.create().
                from( from ).
                to( clean( matcher.group( 2 ) ) ).
                type( flags.get( REDIRECT_CODE_KEY ) != null
                          ? RedirectType.valueOf( Integer.valueOf( flags.get( REDIRECT_CODE_KEY ) ) )
                          : DEFAULT_REDIRECT_TYPE ).
                build();
        }

        return null;
    }


    private static Map<String, String> parseFlags( final String value )
    {
        Map<String, String> flags = Maps.newHashMap();

        if ( Strings.isNullOrEmpty( value ) )
        {
            return flags;
        }

        final List<String> flagKeys = Arrays.asList( value.split( "," ) );

        flagKeys.forEach( f -> {
            final String[] keyValuePair = f.split( "=" );
            if ( keyValuePair.length == 1 )
            {
                flags.put( f, "true" );
            }
            else
            {
                flags.put( keyValuePair[0], keyValuePair[1] );
            }
        } );

        return flags;
    }

    private static String clean( final String original )
    {
        return cleanQuotes( original );
    }

    private static String cleanQuotes( final String original )
    {
        return original.replaceAll( "^\"|\"$", "" );
    }

}
