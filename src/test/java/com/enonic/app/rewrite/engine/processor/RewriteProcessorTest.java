package com.enonic.app.rewrite.engine.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
            add( new SimpleRewriteContext( "/site/default/.+/mySite" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/a" ).
                    to( "/b" ).
                    build() ).
                build() ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( config );

        Assertions.assertEquals( "/b", processor.match( "/site/default/master/mySite/a" ) );
        Assertions.assertEquals( "/b", processor.match( "/site/default/draft/mySite/a" ) );
        Assertions.assertNull( processor.match( "/site/default/mySite/a" ) );
        Assertions.assertNull( processor.match( "/a" ) );
        Assertions.assertNull( processor.match( "" ) );
        Assertions.assertNull( processor.match( null ) );
    }

    @Test
    void wildcardMatch()
    {
        final RewriteEngineConfig config = RewriteEngineConfig.create().
            add( new TestContext( "/site/default/\\w+/mySite" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/a/(.+)" ).
                    to( "/b/$1" ).
                    build() ).
                build() ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( config );

        Assertions.assertEquals( "/b/b", processor.match( "/site/default/master/mySite/a/b" ) );
        Assertions.assertEquals( "/b/fisk", processor.match( "/site/default/draft/mySite/a/fisk" ) );
        Assertions.assertEquals( "/b/fisk/and/more/elements", processor.match( "/site/default/draft/mySite/a/fisk/and/more/elements" ) );
    }


    @Test
    void absolute()
    {
        final RewriteEngineConfig config = RewriteEngineConfig.create().
            add( new TestContext( "/site/default/\\w+/mySite" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/nyheter" ).
                    to( "https://www.vg.no" ).
                    build() ).
                build() ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( config );

        Assertions.assertEquals( "https://www.vg.no", processor.match( "/site/default/master/mySite/nyheter" ) );
    }

}