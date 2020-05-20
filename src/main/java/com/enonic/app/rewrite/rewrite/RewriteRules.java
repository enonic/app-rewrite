package com.enonic.app.rewrite.rewrite;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

public class RewriteRules
    implements Iterable<RewriteRule>
{
    private final List<RewriteRule> ruleList;

    private RewriteRules( final Builder builder )
    {
        ruleList = builder.rules;
    }

    public RewriteRule get( final Integer order )
    {
        for ( final RewriteRule rule : ruleList )
        {
            if ( rule.getOrder() == order )
            {
                return rule;
            }
        }

        return null;
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

    public static Builder from( final RewriteRules rules )
    {
        final Builder builder = new Builder();
        rules.forEach( builder::addRule );
        return builder;
    }

    public int size()
    {
        return this.ruleList.size();
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
}
