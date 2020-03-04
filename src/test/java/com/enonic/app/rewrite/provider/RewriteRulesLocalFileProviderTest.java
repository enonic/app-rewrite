package com.enonic.app.rewrite.provider;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RewriteRulesLocalFileProviderTest
{

    @Test
    void testLoadVHostConfigs()
        throws Exception
    {
        final URL resource = getClass().getResource( "com.enonic.app.rewrite.myvhost.txt" );

        File file = new File( resource.toURI() );

        final RewriteRulesLocalFileProvider provider =
            new RewriteRulesLocalFileProvider( file.getParentFile().toPath(), "com.enonic.app.rewrite.{{vhost}}.txt" );

        final RewriteEngineConfig config = provider.provide();

        assertEquals( 2, config.getRewriteRulesMap().keySet().size() );
        assertNotNull( config.getRewriteRulesMap().get( new RewriteContextKey( "myvhost" ) ) );
        assertNotNull( config.getRewriteRulesMap().get( new RewriteContextKey( "myothervhost" ) ) );

    }
}