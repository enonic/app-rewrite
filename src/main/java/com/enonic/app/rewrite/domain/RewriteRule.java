package com.enonic.app.rewrite.domain;

import java.util.Objects;
import java.util.UUID;

import com.enonic.app.rewrite.redirect.RedirectType;

public class RewriteRule
{
    private final String ruleId;

    private final String from;

    private final RewriteTarget target;

    private final RedirectType type;

    private RewriteRule( final Builder builder )
    {
        ruleId = builder.ruleId != null ? builder.ruleId : UUID.randomUUID().toString();
        from = builder.from;
        target = RewriteTarget.from( builder.target );
        type = builder.type;
    }

    public String getRuleId()
    {
        return ruleId;
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

    public static Builder create()
    {
        return new Builder();
    }

    public static final class Builder
    {

        private String ruleId;

        private String from;

        private String target;

        private RedirectType type;

        private Builder()
        {
        }

        public Builder ruleId( final String ruleId )
        {
            this.ruleId = ruleId;
            return this;
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

        public Builder type( final RedirectType type )
        {
            this.type = type;
            return this;
        }

        private void validate()
        {
            Objects.requireNonNull( this.from, "From cannot be null" );
            Objects.requireNonNull( this.target, "Target cannot be null" );
            Objects.requireNonNull( this.type, "Type cannot be null" );
        }

        public RewriteRule build()
        {
            validate();

            return new RewriteRule( this );
        }

    }

    @Override
    public String toString()
    {
        return "RewriteRule{" + "ruleId='" + ruleId + '\'' + ", from='" + from + '\'' + ", target=" + target + ", type=" + type + '}';
    }

}
