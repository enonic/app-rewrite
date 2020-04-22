package com.enonic.app.rewrite.engine;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.rewrite.RewriteContextKey;

public class RewriteRulesLoadResult
{
    private Map<RewriteContextKey, Integer> loadedRules;

    private RewriteRulesLoadResult( final Builder builder )
    {
        loadedRules = builder.loadedRules;
    }

    public Map<RewriteContextKey, Integer> getLoadedRules()
    {
        return loadedRules;
    }

    public static Builder create()
    {
        return new Builder();
    }


    public static final class Builder
    {
        private Map<RewriteContextKey, Integer> loadedRules = Maps.newHashMap();

        private Builder()
        {
        }

        public Builder add( final RewriteContextKey context, final int size )
        {
            this.loadedRules.put( context, size );
            return this;
        }

        public RewriteRulesLoadResult build()
        {
            return new RewriteRulesLoadResult( this );
        }
    }

    @Override
    public String toString()
    {
        return "RewriteRulesLoadResult{" + "loadedRules=" + loadedRules + '}';
    }
}
