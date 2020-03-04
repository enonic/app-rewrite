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
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                priority( 1 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                priority( 2 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                priority( 2 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                priority( 10 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            build();

        final Iterator<RulePattern> iterator = patterns.iterator();
        Assertions.assertEquals( 1, iterator.next().getPriority() );
        Assertions.assertEquals( 2, iterator.next().getPriority() );
        Assertions.assertEquals( 2, iterator.next().getPriority() );
        Assertions.assertEquals( 3, iterator.next().getPriority() );
        Assertions.assertEquals( 10, iterator.next().getPriority() );
    }
}