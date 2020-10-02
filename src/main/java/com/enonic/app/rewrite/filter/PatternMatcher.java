package com.enonic.app.rewrite.filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PatternMatcher
{
    private Pattern pattern;

    PatternMatcher( final String patternStr )
    {
        this.pattern = Pattern.compile( patternStr );
    }

    boolean match( final String value )
    {
        final Matcher matcher = pattern.matcher( value );

        return matcher.matches();
    }

}
