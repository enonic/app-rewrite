package com.enonic.app.rewrite.domain;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class RewriteRules
    implements Iterable<RewriteRule>
{
    private final List<RewriteRule> ruleList;

    private RewriteRules( final Builder builder )
    {
        ruleList = builder.rules;
    }

    @Override
    public Iterator<RewriteRule> iterator()
    {
        return this.ruleList.iterator();
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private List<RewriteRule> rules = Lists.newArrayList();

        private Builder()
        {
        }

        public Builder rules( final List<RewriteRule> rules )
        {
            this.rules = rules;
            return this;
        }


        public Builder addRule( final RewriteRule rule )
        {
            this.rules.add( rule );
            return this;
        }

        public RewriteRules build()
        {
            return new RewriteRules( this );
        }
    }
}
