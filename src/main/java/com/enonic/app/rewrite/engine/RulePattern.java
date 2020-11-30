package com.enonic.app.rewrite.engine;

import java.util.regex.Pattern;

import com.enonic.app.rewrite.domain.RewriteTarget;
import com.enonic.app.rewrite.redirect.RedirectType;

public class RulePattern
    implements Comparable<RulePattern>
{
    private final RewriteTarget target;

    private final RedirectType type;

    private final int order;

    private final Pattern pattern;

    private RulePattern( final Builder builder )
    {
        target = builder.target;
        type = builder.type;
        order = builder.order;
        this.pattern = Pattern.compile( builder.pattern, Pattern.CASE_INSENSITIVE );
    }

    public static Builder create()
    {
        return new Builder();
    }


    public Pattern getPattern()
    {
        return pattern;
    }

    @Override
    public int compareTo( final RulePattern o )
    {
        return Integer.compare( this.order, o.order );
    }

    RewriteTarget getTarget()
    {
        return target;
    }

    RedirectType getType()
    {
        return type;
    }

    int getOrder()
    {
        return order;
    }

    @Override
    public String toString()
    {
        return "RulePattern{" + "target=" + target + ", type=" + type + ", order=" + order + ", pattern=" + pattern + '}';
    }

    public static final class Builder
    {
        private RewriteTarget target;

        private RedirectType type;

        private int order;

        private String pattern;

        private Builder()
        {
        }

        public Builder target( final String target )
        {
            this.target = RewriteTarget.from( target );
            return this;
        }

        public Builder target( final RewriteTarget target )
        {
            this.target = target;
            return this;
        }

        public Builder type( final RedirectType type )
        {
            this.type = type;
            return this;
        }

        public Builder order( final int priority )
        {
            this.order = priority;
            return this;
        }

        public Builder pattern( final String pattern )
        {
            this.pattern = pattern;
            return this;
        }

        void validate()
        {
            if ( pattern == null )
            {
                throw new IllegalArgumentException( "Pattern must not be null" );
            }
        }

        public RulePattern build()
        {
            validate();
            return new RulePattern( this );
        }
    }
}
