package com.enonic.app.rewrite.mapping;

import java.util.Map;

import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.vhost.RewriteConfigurations;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class RewriteConfigurationsMapper
    implements MapSerializable
{
    private final RewriteConfigurations rewriteConfigurations;

    public RewriteConfigurationsMapper( final RewriteConfigurations rewriteConfigurations )
    {
        this.rewriteConfigurations = rewriteConfigurations;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        final Map<RewriteContextKey, RewriteMappingProvider> configurations = this.rewriteConfigurations.getConfigurations();
        gen.array( "configurations" );
        configurations.forEach( ( rewriteContextKey, provider ) -> {
            gen.map();
            gen.value( "contextKey", rewriteContextKey );
            gen.value( "provider", provider != null ? provider.name() : "" );
            gen.end();
        } );
        gen.end();
    }
}
