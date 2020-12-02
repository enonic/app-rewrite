package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.VirtualHostsDecorator;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class RewriteContextsMapper
    implements MapSerializable
{
    private final VirtualHostsDecorator virtualHosts;

    public RewriteContextsMapper( final VirtualHostsDecorator virtualHosts )
    {
        this.virtualHosts = virtualHosts;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.array( "rewriteContexts" );

        if ( !virtualHosts.getVirtualHostMappings().isEmpty() )
        {
            virtualHosts.getVirtualHostMappings().values().forEach( rewriteContext -> {
                if ( rewriteContext.getMappingProvider() != null )
                {
                    gen.map();
                    new RewriteContextMapper( rewriteContext ).serialize( gen );
                    gen.end();
                }
            } );
        }

        gen.end();
    }

}
