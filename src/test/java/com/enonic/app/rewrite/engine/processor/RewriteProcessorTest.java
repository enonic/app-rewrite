package com.enonic.app.rewrite.engine.processor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RedirectMatch;
import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.domain.SimpleRewriteContext;

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

        final RewriteMapping rewriteMapping = RewriteMapping.create().
            add( new RewriteContextKey( "myContextKey" ), rules ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( rewriteMapping );

        final RedirectMatch match = processor.match( context, "/oldURL" );

        assertNotNull( match );
        assertEquals( "/this/is/source/context/newURL", match.getRedirect().getRedirectTarget().getTargetPath() );
        assertEquals( RedirectType.MOVED_PERMANENTLY, match.getRedirect().getType() );
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

        final RewriteMapping rewriteMapping = RewriteMapping.create().
            add( new RewriteContextKey( "myContextKey" ), rules ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( rewriteMapping );

        final RedirectMatch match = processor.match( context, "/stuff" );

        assertNotNull( match );
        assertEquals( "https://rett24.no", match.getRedirect().getRedirectTarget().getTargetPath() );
        assertEquals( RedirectType.MOVED_PERMANENTLY, match.getRedirect().getType() );
    }

    @Test
    void testSimpleMatch()
    {
        final RewriteMapping config = RewriteMapping.create().
            add( new RewriteContextKey( "myVHost" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/a" ).
                    target( "/b" ).
                    build() ).
                build() ).
            build();

        final RewriteProcessor processor = RewriteProcessor.from( config );

        final SimpleRewriteContext context = new SimpleRewriteContext( "myVHost", "/", "/site/default/master/mySite" );
        Assertions.assertEquals( "/b", processor.match( context,
                                                        "/site/default/master/mySite/a" ).getRedirect().getRedirectTarget().getTargetPath() );
        Assertions.assertNull( processor.match( context, "/site/default/draft/mySite/a" ) );
        Assertions.assertNull( processor.match( context, "/site/default/mySite/a" ) );
        Assertions.assertNull( processor.match( context, "" ) );
        Assertions.assertNull( processor.match( context, null ) );
    }

    @Test
    void wildcardMatch()
    {
        final RewriteMapping config = RewriteMapping.create().
            add( new RewriteContextKey( "myVHost" ), RewriteRules.create().
                addRule( RewriteRule.create().
                    from( "/a/(.+)" ).
                    target( "/b/$1" ).
                    build() ).
                build() ).
            build();

        final SimpleRewriteContext context = new SimpleRewriteContext( "myVHost", "/", "/site/default/master/mySite" );

        final RewriteProcessor processor = RewriteProcessor.from( config );

        Assertions.assertEquals( "/b/b", processor.match( context,
                                                          "/site/default/master/mySite/a/b" ).getRedirect().getRedirectTarget().getTargetPath() );
        Assertions.assertNull( processor.match( context, "/site/default/draft/mySite/a/fisk" ) );
        Assertions.assertEquals( "/b/fisk/and/more/elements", processor.match( context,
                                                                               "/site/default/master/mySite/a/fisk/and/more/elements" ).getRedirect().getRedirectTarget().getTargetPath() );
    }
}