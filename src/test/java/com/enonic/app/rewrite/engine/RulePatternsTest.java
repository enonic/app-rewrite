package com.enonic.app.rewrite.engine;

import java.util.List;

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

        final List<RulePattern> iterator = patterns.getRules();
        Assertions.assertEquals( 1, iterator.get(0).getOrder() );
        Assertions.assertEquals( 2, iterator.get(1).getOrder() );
        Assertions.assertEquals( 2, iterator.get(2).getOrder() );
        Assertions.assertEquals( 3, iterator.get(3).getOrder() );
        Assertions.assertEquals( 10, iterator.get(4).getOrder() );
    }
}
