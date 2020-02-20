package com.enonic.app.rewrite.engine.processor;

import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteTarget;

class RulePattern
    implements Comparable<RulePattern>
{
    private final RewriteTarget target;

    private final String context;

    private final RedirectType type;

    private final int order;

    private final Pattern pattern;

    private RulePattern( final Builder builder )
    {
        target = builder.target;
        context = builder.context;
        type = builder.type;
        order = builder.order;
        this.pattern = Pattern.compile( Paths.get( builder.context, builder.pattern ).toString() );
    }

    static Builder create()
    {
        return new Builder();
    }


    String match( final String value )
    {
        if ( value == null )
        {
            return null;
        }

        final Matcher matcher = this.pattern.matcher( value );
        return matcher.matches() ? matcher.replaceFirst( target.toString() ) : null;
    }

    @Override
    public int compareTo( final RulePattern o )
    {
        return Integer.compare( this.order, o.order );
    }

    String getTarget()
    {
        return target.toString();
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
        return "RulePattern{" + "target=" + target + ", context='" + context + '\'' + ", type=" + type + ", order=" + order + ", pattern=" +
            pattern + '}';
    }

    static final class Builder
    {
        private RewriteTarget target;

        private String context;

        private RedirectType type;

        private int order;

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

        Builder context( final String context )
        {
            this.context = context;
            return this;
        }

        Builder type( final RedirectType type )
        {
            this.type = type;
            return this;
        }

        Builder order( final int order )
        {
            this.order = order;
            return this;
        }


        Builder priority( final int order )
        {
            this.order = order;
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
                throw new IllegalArgumentException( "Patterns must not be null" );
            }
        }

        RulePattern build()
        {
            validate();
            return new RulePattern( this );
        }
    }
}
