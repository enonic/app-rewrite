package com.enonic.app.rewrite.mapping;

import java.util.List;

import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;
import com.enonic.xp.web.vhost.VirtualHost;

public class VirtualHostsMapper
    implements MapSerializable
{
    private final List<VirtualHost> virtualHosts;

    public VirtualHostsMapper( List<VirtualHost> virtualHosts )
    {
        this.virtualHosts = virtualHosts;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.array( "virtualHosts" );
        this.virtualHosts.forEach( mapping -> mapVirtualHost( gen, mapping ) );
        gen.end();
    }

    private void mapVirtualHost( final MapGenerator gen, final VirtualHost virtualHostMapping )
    {
        gen.map();
        gen.value( "type", "virtualHost" );
        gen.value( "name", virtualHostMapping.getName() );
        gen.value( "host", virtualHostMapping.getHost() );
        gen.value( "source", virtualHostMapping.getSource() );
        gen.value( "target", virtualHostMapping.getTarget() );
        gen.value( "defaultIdProviderKey", virtualHostMapping.getDefaultIdProviderKey() );
        gen.end();
    }

}
