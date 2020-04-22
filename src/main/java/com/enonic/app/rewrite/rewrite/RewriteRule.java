package com.enonic.app.rewrite.rewrite;

import com.google.common.base.Preconditions;

import com.enonic.app.rewrite.redirect.RedirectType;

public class RewriteRule
{
    private final String from;

    private final RewriteTarget target;

    private final RedirectType type;

    private final int order;

    private RewriteRule( final Builder builder )
    {
        from = builder.from;
        target = RewriteTarget.from( builder.target );
        type = builder.type;
        this.order = builder.order;
    }

    public String getFrom()
    {
        return from;
    }

    public RewriteTarget getTarget()
    {
        return target;
    }

    public RedirectType getType()
    {
        return type;
    }

    public int getOrder()
    {
        return order;
    }

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private String from;

        private String target;

        private RedirectType type;

        private int order;

        private Builder()
        {
        }

        public Builder from( final String from )
        {
            this.from = from;
            return this;
        }

        public Builder target( final String to )
        {
            this.target = to;
            return this;
        }

        public Builder order( final int order )
        {
            this.order = order;
            return this;
        }

        public Builder type( final RedirectType type )
        {
            this.type = type;
            return this;
        }

        private void validate()
        {
            Preconditions.checkNotNull( this.from, "From cannot be null" );
            Preconditions.checkNotNull( this.target, "Target cannot be null" );
            Preconditions.checkNotNull( this.type, "Type cannot be null" );
        }

        public RewriteRule build()
        {
            return new RewriteRule( this );
        }

    }

    @Override
    public String toString()
    {
        return "RewriteRule{" + "from='" + from + '\'' + ", target=" + target + ", type=" + type + ", order=" + order + '}';
    }
}
