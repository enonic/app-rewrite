package com.enonic.app.rewrite.engine;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.domain.RewriteContext;
import com.enonic.app.rewrite.domain.RewriteRules;

public class RewriteEngineConfig
{
    private Map<RewriteContext, RewriteRules> rewriteRulesMap;

    private RewriteEngineConfig( final Builder builder )
    {
        rewriteRulesMap = builder.rewriteRulesMap;
    }

    public Map<RewriteContext, RewriteRules> getRewriteRulesMap()
    {
        return rewriteRulesMap;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private Map<RewriteContext, RewriteRules> rewriteRulesMap = Maps.newHashMap();

        private Builder()
        {
        }

        public Builder add( final RewriteContext context, final RewriteRules rules )
        {
            rewriteRulesMap.put( context, rules );
            return this;
        }

        public Builder rewriteRulesMap( final Map<RewriteContext, RewriteRules> rewriteRulesMap )
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
