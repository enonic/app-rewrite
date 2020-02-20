package com.enonic.app.rewrite.provider;

import java.io.File;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.enonic.app.rewrite.domain.SimpleRewriteContext;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;
import com.enonic.app.rewrite.vhost.VirtualHostMapping;
import com.enonic.app.rewrite.vhost.VirtualHostResolver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RewriteRulesLocalFileProviderTest
{

    @Test
    void name()
        throws Exception
    {
        final URL resource = getClass().getResource( "com.enonic.app.rewrite.myvhost.txt" );

        final VirtualHostResolver vhostResolver = Mockito.mock( VirtualHostResolver.class );
        Mockito.when( vhostResolver.resolve( "myvhost" ) ).thenReturn( new VirtualHostMapping( "myvhost" ) );
        Mockito.when( vhostResolver.resolve( "myothervhost" ) ).thenReturn( new VirtualHostMapping( "myothervhost" ) );

        File file = new File( resource.toURI() );

        final RewriteRulesLocalFileProvider provider =
            new RewriteRulesLocalFileProvider( file.getParentFile().toPath(), "com.enonic.app.rewrite.{{vhost}}.txt", vhostResolver );

        final RewriteEngineConfig config = provider.provide();

        assertEquals( 2, config.getRewriteRulesMap().keySet().size() );
        assertNotNull( config.getRewriteRulesMap().get( new SimpleRewriteContext( "myvhost" ) ) );
        assertNotNull( config.getRewriteRulesMap().get( new SimpleRewriteContext( "myothervhost" ) ) );

    }
}