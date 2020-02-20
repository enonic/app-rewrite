package com.enonic.app.rewrite.engine.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.domain.SimpleRewriteContext;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;

class RewriteProcessorTest
{
    @Test
    void testSimpleMatch()
    {
        final RewriteEngineConfig config = RewriteEngineConfig.create().
            add( new RewriteContextKey( "myVHost" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/a" ).
                    to( "/b" ).
                    build() ).
                build() ).
            build();

        final RewriteRulesProcessor processor = RewriteRulesProcessor.from( config );

        final SimpleRewriteContext context = new SimpleRewriteContext( "myVHost", "/", "/site/default/master/mySite" );
        Assertions.assertEquals( "/b", processor.match( context, "/site/default/master/mySite/a" ) );
        Assertions.assertNull( processor.match( context, "/site/default/draft/mySite/a" ) );
        Assertions.assertNull( processor.match( context, "/site/default/mySite/a" ) );
//        Assertions.assertNull( processor.match( context, "/a" ) );
        Assertions.assertNull( processor.match( context, "" ) );
        Assertions.assertNull( processor.match( context, null ) );
    }

    @Test
    void wildcardMatch()
    {
        final RewriteEngineConfig config = RewriteEngineConfig.create().
            add( new RewriteContextKey( "myVHost" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/a/(.+)" ).
                    to( "/b/$1" ).
                    build() ).
                build() ).
            build();

        final SimpleRewriteContext context = new SimpleRewriteContext( "myVHost", "/", "/site/default/master/mySite" );

        final RewriteRulesProcessor processor = RewriteRulesProcessor.from( config );

        Assertions.assertEquals( "/b/b", processor.match( context, "/site/default/master/mySite/a/b" ) );
        Assertions.assertNull( processor.match( context, "/site/default/draft/mySite/a/fisk" ) );
        Assertions.assertEquals( "/b/fisk/and/more/elements",
                                 processor.match( context, "/site/default/master/mySite/a/fisk/and/more/elements" ) );
    }

}