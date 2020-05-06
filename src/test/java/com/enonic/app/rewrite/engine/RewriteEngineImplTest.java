package com.enonic.app.rewrite.engine;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.enonic.app.rewrite.MockHttpRequest;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
import com.enonic.xp.web.vhost.VirtualHost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RewriteEngineImplTest
{

    @Test
    void testSimpleRewrite()
    {
        final RewriteRules rules = RewriteRules.create().
            addRule( RewriteRule.create().
                from( "/oldUrl" ).
                target( "/newUrl" ).
                type( RedirectType.MOVED_PERMANENTLY ).
                build() ).
            build();

        final RewriteMappings rewriteMappings = RewriteMappings.create().
            add( RewriteMapping.create().
                contextKey( new RewriteContextKey( "myvhost" ) ).
                rewriteRules( rules ).
                build() ).
            build();

        final RewriteEngine rewriteEngine = new RewriteEngine();
        rewriteEngine.load( rewriteMappings );

        final VirtualHost vhost = Mockito.mock( VirtualHost.class );
        Mockito.when( vhost.getName() ).thenReturn( "myvhost" );
        Mockito.when( vhost.getSource() ).thenReturn( "/" );
        Mockito.when( vhost.getTarget() ).thenReturn( "/site/default/master/mysite" );

        final HttpServletRequest request = MockHttpRequest.create().
            contextPath( "/" ).
            url( "https://www.mysite.ost/site/default/master/mysite/oldUrl" ).
            vhost( vhost ).
            build().getRequest();

        final RedirectMatch match = rewriteEngine.process( request );

        assertNotNull( match );
        assertEquals( "/newUrl", match.getRedirect().getRedirectTarget().getTargetPath() );
    }

    @Test
    void testWildcard()
    {
        final RewriteRules rules = RewriteRules.create().
            addRule( RewriteRule.create().
                from( "/oldUrl/(.*)" ).
                target( "/newUrl/$1" ).
                type( RedirectType.MOVED_PERMANENTLY ).
                build() ).
            build();

        final RewriteMappings rewriteMappings = RewriteMappings.create().
            add( RewriteMapping.create().
                contextKey( new RewriteContextKey( "myvhost" ) ).
                rewriteRules( rules ).
                build() ).
            build();

        final RewriteEngine rewriteEngine = new RewriteEngine();
        rewriteEngine.load( rewriteMappings );

        final VirtualHost vhost = Mockito.mock( VirtualHost.class );
        Mockito.when( vhost.getName() ).thenReturn( "myvhost" );
        Mockito.when( vhost.getSource() ).thenReturn( "/" );
        Mockito.when( vhost.getTarget() ).thenReturn( "/site/default/master/mysite" );

        final HttpServletRequest request = MockHttpRequest.create().
            contextPath( "/" ).
            url( "https://www.mysite.ost/site/default/master/mysite/oldUrl/child" ).
            vhost( vhost ).
            build().getRequest();

        final RedirectMatch match = rewriteEngine.process( request );

        assertNotNull( match );
        assertEquals( "/newUrl/child", match.getRedirect().getRedirectTarget().getTargetPath() );
    }


}