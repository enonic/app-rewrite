package com.enonic.app.rewrite.engine.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.domain.SimpleRewriteContext;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RewriteProcessorTest
{
    @Test
    void test1()
    {
        final SimpleRewriteContext context =
            new SimpleRewriteContext( "myContextKey", "/this/is/source/context", "/doesnt/matter/much/here" );

        final RewriteRules rules = RewriteRules.create().
            addRule( RewriteRule.create().
                type( RedirectType.MOVED_PERMANENTLY ).
                from( "/oldURL" ).
                target( "/newURL" ).
                build() ).
            build();

        final RewriteEngineConfig rewriteEngineConfig = RewriteEngineConfig.create().
            add( new RewriteContextKey( "myContextKey" ), rules ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( rewriteEngineConfig );

        final Redirect redirect = processor.match( context, "/oldURL" );

        assertNotNull( redirect );
        assertEquals( "/this/is/source/context/newURL", redirect.getRedirectTarget().getTargetPath() );
        assertEquals( RedirectType.MOVED_PERMANENTLY, redirect.getType() );
    }

    @Test
    void testExternalTarget()
    {
        final SimpleRewriteContext context =
            new SimpleRewriteContext( "myContextKey", "/this/is/source/context", "/doesnt/matter/much/here" );

        final RewriteRules rules = RewriteRules.create().
            addRule( RewriteRule.create().
                type( RedirectType.MOVED_PERMANENTLY ).
                from( "/stuff" ).
                target( "https://rett24.no" ).
                build() ).
            build();

        final RewriteEngineConfig rewriteEngineConfig = RewriteEngineConfig.create().
            add( new RewriteContextKey( "myContextKey" ), rules ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( rewriteEngineConfig );

        final Redirect redirect = processor.match( context, "/stuff" );

        assertNotNull( redirect );
        assertEquals( "https://rett24.no", redirect.getRedirectTarget().getTargetPath() );
        assertEquals( RedirectType.MOVED_PERMANENTLY, redirect.getType() );
    }

    @Test
    void testSimpleMatch()
    {
        final RewriteEngineConfig config = RewriteEngineConfig.create().
            add( new RewriteContextKey( "myVHost" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/a" ).
                    target( "/b" ).
                    build() ).
                build() ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( config );

        final SimpleRewriteContext context = new SimpleRewriteContext( "myVHost", "/", "/site/default/master/mySite" );
        Assertions.assertEquals( "/b", processor.match( context, "/site/default/master/mySite/a" ).getRedirectTarget().getTargetPath() );
        Assertions.assertNull( processor.match( context, "/site/default/draft/mySite/a" ) );
        Assertions.assertNull( processor.match( context, "/site/default/mySite/a" ) );
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
                    target( "/b/$1" ).
                    build() ).
                build() ).
            build();

        final SimpleRewriteContext context = new SimpleRewriteContext( "myVHost", "/", "/site/default/master/mySite" );

        final RewriteProcessor processor = RewriteProcessor.from( config );

        Assertions.assertEquals( "/b/b",
                                 processor.match( context, "/site/default/master/mySite/a/b" ).getRedirectTarget().getTargetPath() );
        Assertions.assertNull( processor.match( context, "/site/default/draft/mySite/a/fisk" ) );
        Assertions.assertEquals( "/b/fisk/and/more/elements", processor.match( context,
                                                                               "/site/default/master/mySite/a/fisk/and/more/elements" ).getRedirectTarget().getTargetPath() );
    }
}