package com.enonic.app.rewrite.provider;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.provider.file.FileNameMatcher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNameMatcherTest
{
    @Test
    void testMatcher()
    {
        final Path path = Path.of( "/Users/someuser/somefolder/fisk/ost/onions/com.enonic.app.rewrite.myvhost.txt" );
        final String pattern = "com.enonic.app.rewrite.(\\w+)\\.txt";

        assertTrue( FileNameMatcher.matches( path, pattern ) );
        assertEquals( "myvhost", FileNameMatcher.getMatch( path, pattern, 1 ) );
    }
}
