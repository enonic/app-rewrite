package com.enonic.app.rewrite.engine;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.MockHttpRequest;
import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.domain.SimpleRewriteContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewriteEngineImplTest
{
    @Test
    void testSimpleRewrite()
    {
        final RewriteRules rules = RewriteRules.create().
            addRule( RewriteRule.create().
                from( "/oldUrl" ).
                to( "/newUrl" ).
                type( RedirectType.MOVED_PERMANENTLY ).
                build() ).
            build();

        final RewriteEngineConfig engineConfig = RewriteEngineConfig.create().
            add( new SimpleRewriteContext( "/site/default/master/mysite" ), rules ).
            build();

        final RewriteEngine rewriteEngine = new RewriteEngine( engineConfig );
        final String processed = rewriteEngine.process( MockHttpRequest.create().
            contextPath( "/" ).
            url( "https://www.mysite.ost/site/default/master/mysite/oldUrl" ).
            build().getRequest() );

        assertEquals( "/newUrl", processed );
    }
}