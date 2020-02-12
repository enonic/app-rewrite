package com.enonic.app.rewrite.provider;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNameMatcherTest
{

    @Test
    void testMatcher()
    {
        final Path path = Paths.get(
            "/Users/rmy/Dev/git/XP-APPS/app-redirect/out/test/resources/com/enonic/app/rewrite/provider/com.enonic.app.rewrite.myvhost.txt" );
        final String pattern = "com.enonic.app.rewrite.(\\w+)\\.txt";

        assertTrue( FileNameMatcher.matches( path, pattern ) );

    }
}