package com.enonic.app.rewrite.engine;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.enonic.app.rewrite.MockHttpRequest;
import com.enonic.app.rewrite.domain.RedirectMatch;
import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
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

        final RewriteMapping engineConfig = RewriteMapping.create().
            add( new RewriteContextKey( "myvhost" ), rules ).
            build();

        final RewriteEngine rewriteEngine = new RewriteEngine( engineConfig );

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

        final RewriteMapping engineConfig = RewriteMapping.create().
            add( new RewriteContextKey( "myvhost" ), rules ).
            build();

        final RewriteEngine rewriteEngine = new RewriteEngine( engineConfig );

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