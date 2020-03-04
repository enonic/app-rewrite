package com.enonic.app.rewrite.provider;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;

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

        final RewriteMappingLocalFileProvider provider = RewriteMappingLocalFileProvider.create().
            base( file.getParentFile().toPath() ).
            ruleFilePattern( "com.enonic.app.rewrite.{{vhost}}.txt" ).
            build();

        final RewriteMapping config = provider.get();

        assertEquals( 2, config.getRewriteRulesMap().keySet().size() );
        assertNotNull( config.getRewriteRulesMap().get( new RewriteContextKey( "myvhost" ) ) );
        assertNotNull( config.getRewriteRulesMap().get( new RewriteContextKey( "myothervhost" ) ) );

    }
}