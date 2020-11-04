package com.enonic.app.rewrite.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RewriteRules
{
    private final List<RewriteRule> ruleList;

    private RewriteRules( final Builder builder )
    {
        ruleList = builder.rules;
    }

    public List<RewriteRule> getRuleList()
    {
        return ruleList;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static Builder from( final RewriteRules rules )
    {
        final Builder builder = new Builder();

        rules.ruleList.forEach( builder::addRule );

        return builder;
    }

    public int size()
    {
        return this.ruleList.size();
    }

    public static final class Builder
    {
        private List<RewriteRule> rules = new ArrayList<>();

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

        public Builder addRule( final int position, final RewriteRule rule )
        {
            this.rules.add( position, rule );
            return this;
        }

        public Builder addRule( final RewriteRule rule, boolean first )
        {
            if ( first )
            {
                this.rules.add( 0, rule );
            }
            else
            {
                this.rules.add( rule );
            }

            return this;
        }

        public RewriteRules build()
        {
            return new RewriteRules( this );
        }
    }


    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final RewriteRules that = (RewriteRules) o;
        return Objects.equals( ruleList, that.ruleList );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( ruleList );
    }

    @Override
    public String toString()
    {
        return "RewriteRules{" + "ruleList=" + ruleList + '}';
    }
}
