package com.enonic.app.rewrite.provider.file;

import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameMatcher
{
    public static boolean matches( final Path path, final String pattern )
    {
        return getFileNameString( path ).matches( pattern );
    }

    private static String getFileNameString( final Path path )
    {
        return path.getFileName().toString();
    }

    public static String getMatch( final Path path, final String regExp, final int group )
    {
        if ( !matches( path, regExp ) )
        {
            return null;
        }

        final Pattern pattern = Pattern.compile( regExp );

        final Matcher matcher = pattern.matcher( getFileNameString( path ) );
        if ( matcher.matches() )
        {
            return matcher.group( group );
        }

        return null;
    }

}
