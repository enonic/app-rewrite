package com.enonic.app.rewrite.mapping;

import java.util.Map;
import java.util.Optional;

import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.vhost.RewriteConfigurations;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class RewriteConfigurationsMapper
    implements MapSerializable
{
    private final Map<RewriteContextKey, Optional<RewriteMappingProvider>> rewriteConfigurations;

    public RewriteConfigurationsMapper( final Map<RewriteContextKey, Optional<RewriteMappingProvider>> rewriteConfigurations )
    {
        this.rewriteConfigurations = rewriteConfigurations;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.array( "configurations" );
        rewriteConfigurations.forEach( ( rewriteContextKey, provider ) -> {
            gen.map();
            gen.value( "contextKey", rewriteContextKey );
            gen.value( "provider", provider.isPresent() ? provider.get().name() : "" );
            gen.end();
        } );
        gen.end();
    }
}
