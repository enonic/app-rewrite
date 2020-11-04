package com.enonic.app.rewrite;

import java.util.Map;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class VirtualHostsDecorator
{
    private final Map<RewriteContextKey, VirtualHostWrapper> virtualHostMappings;

    public VirtualHostsDecorator( final Map<RewriteContextKey, VirtualHostWrapper> virtualHostMappings )
    {
        this.virtualHostMappings = virtualHostMappings;
    }

    public Map<RewriteContextKey, VirtualHostWrapper> getVirtualHostMappings()
    {
        return virtualHostMappings;
    }

    public VirtualHostWrapper getVirtualHostMapping( final RewriteContextKey contextKey )
    {
        return virtualHostMappings.get( contextKey );
    }

}
