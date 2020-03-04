package com.enonic.app.rewrite.domain;

import java.util.Map;

import com.google.common.collect.Maps;

public class RewriteMapping
{
    private Map<RewriteContextKey, RewriteRules> rewriteRulesMap;

    private RewriteMapping( final Builder builder )
    {
        rewriteRulesMap = builder.rewriteRulesMap;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public Map<RewriteContextKey, RewriteRules> getRewriteRulesMap()
    {
        return rewriteRulesMap;
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

        public RewriteMapping build()
        {
            return new RewriteMapping( this );
        }
    }
}
