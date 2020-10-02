package com.enonic.app.rewrite.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

class RulePatterns
    implements Iterable<RulePattern>
{
    private final List<RulePattern> rules;

    private RulePatterns( final Builder builder )
    {
        rules = builder.rules;
    }

    static Builder create()
    {
        return new Builder();
    }

    @Override
    public Iterator<RulePattern> iterator()
    {
        return this.rules.iterator();
    }

    int size()
    {
        return this.rules.size();
    }

    static final class Builder
    {
        private List<RulePattern> rules = new ArrayList<>();

        private Builder()
        {
        }

        Builder rules( final List<RulePattern> rules )
        {
            this.rules = rules;
            return this;
        }

        Builder rule( final RulePattern rulePattern )
        {
            this.rules.add( rulePattern );
            return this;
        }

        RulePatterns build()
        {
            Collections.sort( rules );
            return new RulePatterns( this );
        }
    }
}
