package com.enonic.app.rewrite.provider.repo;

import com.enonic.app.rewrite.rewrite.RewriteRule;

public class RuleNodeName
{
    private static final String NAME_PREFIX = "rule_";

    private final String name;

    private RuleNodeName( final String name )
    {
        this.name = name;
    }

    public static RuleNodeName from( final RewriteRule rule )
    {
        return new RuleNodeName( NAME_PREFIX + rule.getOrder() );
    }

    public String getName()
    {
        return this.name;
    }

}
