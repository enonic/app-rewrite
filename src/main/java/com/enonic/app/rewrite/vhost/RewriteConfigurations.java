package com.enonic.app.rewrite.vhost;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.domain.RewriteContextKey;

public class RewriteConfigurations
{
    private Map<RewriteContextKey, RewriteMappingProvider> configurations;

    private RewriteConfigurations( final Builder builder )
    {
        configurations = builder.configurations;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public RewriteMappingProvider getProvider( final RewriteContextKey contextKey )
    {
        return this.configurations.get( contextKey );
    }

    public Map<RewriteContextKey, RewriteMappingProvider> getConfigurations()
    {
        return configurations;
    }

    public static final class Builder
    {
        private Map<RewriteContextKey, RewriteMappingProvider> configurations = Maps.newHashMap();

        private Builder()
        {
        }

        public Builder add( final RewriteContextKey contextKey, final RewriteMappingProvider provider )
        {
            this.configurations.put( contextKey, provider );
            return this;
        }

        public RewriteConfigurations build()
        {
            return new RewriteConfigurations( this );
        }
    }
}
