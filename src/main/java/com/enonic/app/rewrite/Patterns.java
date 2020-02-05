package com.enonic.app.rewrite;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Patterns
{
    private Pattern[] patterns;

    public Patterns( final List<String> collection )
    {
        patterns = new Pattern[collection.size()];
        for ( int i = 0; i < collection.size(); i++ )
        {
            String patternStr = (String) collection.get( i );
            patterns[i] = Pattern.compile( patternStr );
        }
    }

    public boolean anyMatch( final String value )
    {
        for ( Pattern pattern : patterns )
        {
            Matcher matcher = pattern.matcher( value );
            if ( matcher.find() )
            {
                return true;
            }
        }
        return false;
    }

}
