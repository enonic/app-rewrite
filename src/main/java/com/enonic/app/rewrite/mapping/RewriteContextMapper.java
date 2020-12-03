package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.VirtualHostWrapper;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;
import com.enonic.xp.web.vhost.VirtualHost;

public class RewriteContextMapper
    implements MapSerializable
{

    private final VirtualHostWrapper virtualHostWrapper;

    public RewriteContextMapper( final VirtualHostWrapper virtualHostWrapper )
    {
        this.virtualHostWrapper = virtualHostWrapper;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.map( "rewriteContext" );

        gen.value( "name", virtualHostWrapper.getContextKey().toString() );
        gen.value( "type", "virtualHost" );
        gen.value( "active", virtualHostWrapper.isActive() );

        final VirtualHost virtualHost = virtualHostWrapper.getVirtualHost();

        if ( virtualHost != null )
        {
            gen.value( "host", virtualHost.getHost() );
            gen.value( "source", virtualHost.getSource() );
            gen.value( "target", virtualHost.getTarget() );
            gen.value( "defaultIdProviderKey", virtualHost.getDefaultIdProviderKey() );
        }

        gen.end();
    }
}
