package com.enonic.app.rewrite.engine.processor;

import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RulePatternsTest
{


    @Test
    void testRuleOrder()
    {

        final RulePatterns patterns = RulePatterns.create().
            rule( RulePattern.create().
                priority( 3 ).
                target( "target" ).
                build() ).
            rule( RulePattern.create().
                priority( 1 ).
                target( "target" ).
                build() ).
            rule( RulePattern.create().
                priority( 2 ).
                target( "target" ).
                build() ).
            rule( RulePattern.create().
                priority( 2 ).
                target( "target" ).
                build() ).
            rule( RulePattern.create().
                priority( 10 ).
                target( "target" ).
                build() ).
            build();

        final Iterator<RulePattern> iterator = patterns.iterator();
        Assertions.assertEquals( 1, iterator.next().getOrder() );
        Assertions.assertEquals( 2, iterator.next().getOrder() );
        Assertions.assertEquals( 2, iterator.next().getOrder() );
        Assertions.assertEquals( 3, iterator.next().getOrder() );
        Assertions.assertEquals( 10, iterator.next().getOrder() );

    }
}