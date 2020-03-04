package com.enonic.app.rewrite.engine.processor;

import java.util.regex.Pattern;

import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteTarget;

class RulePattern
    implements Comparable<RulePattern>
{
    private final RewriteTarget target;

    private final RedirectType type;

    private final int priority;

    private final Pattern pattern;

    private RulePattern( final Builder builder )
    {
        target = builder.target;
        type = builder.type;
        priority = builder.priority;
        this.pattern = Pattern.compile( builder.pattern );
    }

    static Builder create()
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
        return Integer.compare( this.priority, o.priority );
    }

    RewriteTarget getTarget()
    {
        return target;
    }

    RedirectType getType()
    {
        return type;
    }

    int getPriority()
    {
        return priority;
    }

    @Override
    public String toString()
    {
        return "RulePattern{" + "target=" + target + ", type=" + type + ", priority=" + priority + ", pattern=" + pattern + '}';
    }

    static final class Builder
    {
        private RewriteTarget target;

        private RedirectType type;

        private int priority;

        private String pattern;

        private Builder()
        {
        }

        Builder target( final String target )
        {
            this.target = RewriteTarget.from( target );
            return this;
        }


        Builder target( final RewriteTarget target )
        {
            this.target = target;
            return this;
        }

        Builder type( final RedirectType type )
        {
            this.type = type;
            return this;
        }

        Builder priority( final int priority )
        {
            this.priority = priority;
            return this;
        }

        Builder pattern( final String pattern )
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

        RulePattern build()
        {
            validate();
            return new RulePattern( this );
        }
    }
}
