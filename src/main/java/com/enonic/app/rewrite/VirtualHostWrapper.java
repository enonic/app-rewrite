package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.xp.web.vhost.VirtualHost;

public class VirtualHostWrapper
{

    private final RewriteContextKey contextKey;

    private final VirtualHost virtualHost;

    private final RewriteMappingProvider mappingProvider;

    private final boolean disabled;

    public VirtualHostWrapper( final Builder builder )
    {
        this.contextKey = builder.contextKey;
        this.virtualHost = builder.virtualHost;
        this.mappingProvider = builder.mappingProvider;
        this.disabled = builder.disabled;
    }

    public RewriteContextKey getContextKey()
    {
        return contextKey;
    }

    public VirtualHost getVirtualHost()
    {
        return virtualHost;
    }

    public RewriteMappingProvider getMappingProvider()
    {
        return mappingProvider;
    }

    public boolean isDisabled()
    {
        return disabled;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static class Builder
    {

        private RewriteContextKey contextKey;

        private VirtualHost virtualHost;

        private RewriteMappingProvider mappingProvider;

        private boolean disabled;

        public Builder mappingProvider( final RewriteMappingProvider mappingProvider )
        {
            this.mappingProvider = mappingProvider;
            return this;
        }

        public Builder contextKey( final RewriteContextKey contextKey )
        {
            this.contextKey = contextKey;
            return this;
        }

        public Builder virtualHost( final VirtualHost virtualHost )
        {
            this.virtualHost = virtualHost;
            return this;
        }

        public Builder disabled( final boolean disabled )
        {
            this.disabled = disabled;
            return this;
        }


        public VirtualHostWrapper build()
        {
            return new VirtualHostWrapper( this );
        }

    }

}
