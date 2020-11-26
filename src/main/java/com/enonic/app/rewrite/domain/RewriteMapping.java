package com.enonic.app.rewrite.domain;

import java.util.Objects;

public class RewriteMapping
{
    private final RewriteContextKey contextKey;

    private final RewriteRules rewriteRules;

    private RewriteMapping( final Builder builder )
    {
        contextKey = builder.contextKey;
        rewriteRules = builder.rewriteRules;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public RewriteContextKey getContextKey()
    {
        return contextKey;
    }

    public RewriteRules getRewriteRules()
    {
        return rewriteRules;
    }

    public static final class Builder
    {
        private RewriteContextKey contextKey;

        private RewriteRules rewriteRules;

        private Builder()
        {
        }

        public Builder contextKey( final RewriteContextKey contextKey )
        {
            this.contextKey = contextKey;
            return this;
        }

        public Builder rewriteRules( final RewriteRules rewriteRules )
        {
            this.rewriteRules = rewriteRules;
            return this;
        }

        public RewriteMapping build()
        {
            return new RewriteMapping( this );
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
        final RewriteMapping that = (RewriteMapping) o;
        return Objects.equals( contextKey, that.contextKey ) && Objects.equals( rewriteRules, that.rewriteRules );
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( contextKey, rewriteRules );
    }


    @Override
    public String toString()
    {
        return "RewriteMapping{" + "contextKey=" + contextKey + ", rewriteRules=" + rewriteRules + '}';
    }
}
