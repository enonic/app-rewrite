package com.enonic.app.rewrite.provider.file;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;

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
            ruleFileNameTemplate( "com.enonic.app.rewrite.{{vhost}}.txt" ).
            build();

        final RewriteMapping rewriteMapping = provider.getRewriteMapping( new RewriteContextKey( "myvhost" ) );

        // assertTrue( containsContextKey( mappingList, new RewriteContextKey( "myvhost" ) ) );
        // assertTrue( containsContextKey( mappingList, new RewriteContextKey( "myothervhost" ) ) );
    }

    private boolean containsContextKey( List<RewriteMapping> mappings, final RewriteContextKey contextKey )
    {
        return mappings.stream().map( RewriteMapping::getContextKey ).collect( Collectors.toList() ).
            contains( contextKey );
    }

}
