package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.requesttester.VirtualHostMapping;
import com.enonic.app.rewrite.requesttester.VirtualHostMappings;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class VirtualHostsMapper
    implements MapSerializable
{
    private final VirtualHostMappings virtualHostMappings;

    public VirtualHostsMapper( final VirtualHostMappings virtualHostMappings )
    {
        this.virtualHostMappings = virtualHostMappings;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.array( "virtualHosts" );
        this.virtualHostMappings.forEach( mapping -> {
            mapVirtualHost( gen, mapping );
        } );
        gen.end();
    }

    private void mapVirtualHost( final MapGenerator gen, final VirtualHostMapping virtualHostMapping )
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
