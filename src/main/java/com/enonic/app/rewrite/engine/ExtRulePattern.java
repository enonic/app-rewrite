package com.enonic.app.rewrite.engine;

public class ExtRulePattern
{
    private final int position;

    private final RulePattern rulePattern;

    public ExtRulePattern( final int position, final RulePattern rulePattern )
    {
        this.position = position;
        this.rulePattern = rulePattern;
    }

    public int getPosition()
    {
        return position;
    }

    public RulePattern getRulePattern()
    {
        return rulePattern;
    }

}
