package com.enonic.app.rewrite.provider;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;

class RewriteRulesLocalFileProviderTest
{


    @Test
    void name()
        throws Exception
    {
        final URL resource = getClass().getResource( "com.enonic.app.rewrite.myvhost.txt" );

        File file = new File( resource.toURI() );

        final RewriteRulesLocalFileProvider provider =
            new RewriteRulesLocalFileProvider( file.getParentFile().toPath(), "com.enonic.app.rewrite.{{vhost}}.txt" );

        System.out.println( provider );

    }
}