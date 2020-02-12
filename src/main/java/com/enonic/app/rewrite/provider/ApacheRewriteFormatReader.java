package com.enonic.app.rewrite.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

import com.enonic.app.rewrite.domain.RewriteRule;

class ApacheRewriteFormatReader
{
    private final static Pattern apacheFormat = Pattern.compile( "^RewriteRule\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)" );

    static RewriteRule read( final String value )
    {
        if ( Strings.isNullOrEmpty( value ) )
        {
            return null;
        }

        final Matcher matcher = apacheFormat.matcher( value );
        if ( matcher.matches() )
        {
            return RewriteRule.create().
                from( clean( matcher.group( 1 ) ) ).
                to( clean( matcher.group( 2 ) ) ).
                build();
        }

        return null;
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
