package com.enonic.app.rewrite.engine;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.enonic.app.rewrite.MockHttpRequest;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.xp.web.vhost.VirtualHost;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RewriteEngineImplTest
{

    private VirtualHost vhost;

    @BeforeEach
    void setUp()
    {
        vhost = Mockito.mock( VirtualHost.class );
        Mockito.when( vhost.getName() ).thenReturn( "myvhost" );
        Mockito.when( vhost.getSource() ).thenReturn( "/" );
        Mockito.when( vhost.getTarget() ).thenReturn( "/site/default/master/mysite" );
    }

    @Test
    void testSimpleRewrite()
    {
        final List<RewriteMapping> rewriteMappings = prepareRewriteMappings( "/oldUrl", "/newUrl", RedirectType.MOVED_PERMANENTLY );

        final RewriteEngine rewriteEngine = new RewriteEngine();
        rewriteEngine.load( rewriteMappings );

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
        final List<RewriteMapping> rewriteMappings = prepareRewriteMappings( "/oldUrl/(.*)", "/newUrl/$1", RedirectType.MOVED_PERMANENTLY );

        final RewriteEngine rewriteEngine = new RewriteEngine();
        rewriteEngine.load( rewriteMappings );

        final HttpServletRequest request = MockHttpRequest.create().
            contextPath( "/" ).
            url( "https://www.mysite.ost/site/default/master/mysite/oldUrl/child" ).
            vhost( vhost ).
            build().getRequest();

        final RedirectMatch match = rewriteEngine.process( request );

        assertNotNull( match );
        assertEquals( "/newUrl/child", match.getRedirect().getRedirectTarget().getTargetPath() );
    }

    @Test
    void testExtendedWildcard()
    {
        final List<RewriteMapping> rewriteMappings =
            prepareRewriteMappings( "/oldUrl/(.*)/resource/(.*)", "/newUrl/$1/res/$2", RedirectType.MOVED_PERMANENTLY );

        final RewriteEngine rewriteEngine = new RewriteEngine();
        rewriteEngine.load( rewriteMappings );

        final HttpServletRequest request = MockHttpRequest.create().
            contextPath( "/" ).
            url( "https://www.mysite.ost/site/default/master/mysite/oldUrl/child/resource/child2" ).
            vhost( vhost ).
            build().getRequest();

        final RedirectMatch match = rewriteEngine.process( request );

        assertNotNull( match );
        assertEquals( "/newUrl/child/res/child2", match.getRedirect().getRedirectTarget().getTargetPath() );
    }

    @Test
    void testEncodedPath()
    {
        final String originalUrl = "brukerstøtte";

        final List<RewriteMapping> rewriteMappings = prepareRewriteMappings( "/" + originalUrl, "/support", RedirectType.FOUND );

        final RewriteEngine rewriteEngine = new RewriteEngine();
        rewriteEngine.load( rewriteMappings );

        final HttpServletRequest request = MockHttpRequest.create().
            contextPath( "/" ).
            url( "https://www.mysite.ost/site/default/master/mysite/" + originalUrl).
            vhost( vhost ).
            build().getRequest();

        final RedirectMatch match = rewriteEngine.process( request );

        assertNotNull( match );
        assertEquals( "/support", match.getRedirect().getRedirectTarget().getTargetPath() );
    }

    private List<RewriteMapping> prepareRewriteMappings( final String source, final String target, final RedirectType redirectType )
    {
        final RewriteRules rules = RewriteRules.create().
            addRule( RewriteRule.create().
                from( source ).
                target( target ).
                type( redirectType ).
                build() ).
            build();

        return List.of( RewriteMapping.create().
            contextKey( new RewriteContextKey( "myvhost" ) ).
            rewriteRules( rules ).
            build() );
    }

}
