package com.enonic.app.rewrite.engine;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteRules;

public class RewriteEngineConfig
{
    private Map<RewriteContextKey, RewriteRules> rewriteRulesMap;

    private RewriteEngineConfig( final Builder builder )
    {
        rewriteRulesMap = builder.rewriteRulesMap;
    }

    public Map<RewriteContextKey, RewriteRules> getRewriteRulesMap()
    {
        return rewriteRulesMap;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private Map<RewriteContextKey, RewriteRules> rewriteRulesMap = Maps.newHashMap();

        private Builder()
        {
        }

        public Builder add( final RewriteContextKey context, final RewriteRules rules )
        {
            rewriteRulesMap.put( context, rules );
            return this;
        }

        public Builder rewriteRulesMap( final Map<RewriteContextKey, RewriteRules> rewriteRulesMap )
        {
            this.rewriteRulesMap = rewriteRulesMap;
            return this;
        }

        public RewriteEngineConfig build()
        {
            return new RewriteEngineConfig( this );
        }
    }
}
