package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.vhost.VirtualHostMapping;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class RewriteContextMapper
    implements MapSerializable
{
    private final VirtualHostMapping virtualHostMapping;

    public RewriteContextMapper( final VirtualHostMapping virtualHostMapping )
    {
        this.virtualHostMapping = virtualHostMapping;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.map( "rewriteContext" );
        gen.value( "name", virtualHostMapping.getName() );
        gen.value( "type", "virtualHost" );
        gen.value( "host", virtualHostMapping.getHost() );
        gen.value( "source", virtualHostMapping.getSource() );
        gen.value( "target", virtualHostMapping.getTarget() );
        gen.value( "defaultIdProviderKey", virtualHostMapping.getDefaultIdProviderKey() );
        gen.end();
    }
}
