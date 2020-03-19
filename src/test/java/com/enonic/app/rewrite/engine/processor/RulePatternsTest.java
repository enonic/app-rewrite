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
                order( 3 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                order( 1 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                order( 2 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                order( 2 ).
                target( "target" ).
                pattern( "/fisk" ).
                build() ).
            rule( RulePattern.create().
                order( 10 ).
                target( "target" ).
                pattern( "/fisk" ).
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