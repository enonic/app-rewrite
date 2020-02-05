package com.enonic.app.rewrite;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.NormalRule;

import com.google.common.collect.Lists;

import com.enonic.app.rewrite.provider.RewriteRulesProvider;
import com.enonic.app.rewrite.provider.RewriteRulesProviderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewriteEngineImplTest
{
    private RewriteRulesProviderFactory rewriteRuleProviderFactory;

    private RewriteRulesProvider rewriteRulesProvider;

    private RewriteEngineImpl rewriteEngine;

    @BeforeEach
    void setUp()
    {
        this.rewriteRuleProviderFactory = Mockito.mock( RewriteRulesProviderFactory.class );
        this.rewriteRulesProvider = Mockito.mock( RewriteRulesProvider.class );
        Mockito.when( this.rewriteRuleProviderFactory.get( Mockito.any() ) ).thenReturn( rewriteRulesProvider );

        this.rewriteEngine = new RewriteEngineImpl();
        rewriteEngine.setRewriteRulesProviderFactory( rewriteRuleProviderFactory );
    }

    @Test
    void testRedirectProcessing()
    {
        final Conf config = buildTestConf( Redirects.create().
            redirect( new Redirect( "/a/b", "/c/d" ) ).
            build() );

        Mockito.when( this.rewriteRulesProvider.get() ).thenReturn( config );
        rewriteEngine.activate();

        final MockHttpRequest mockRequest = MockHttpRequest.create().
            contextPath( "/" ).
            url( "https://test.com/a/b" ).
            build();

        final String process = rewriteEngine.process( mockRequest.getRequest(), Mockito.mock( HttpServletResponse.class ) );

        assertEquals( "/c/d", process );
    }

    private Conf buildTestConf( final Redirects redirects )
    {
        final Conf conf = new Conf();

        for ( final Redirect redirect : redirects )
        {
            final NormalRule rule = new NormalRule();
            rule.setFrom( redirect.from );
            rule.setTo( redirect.to );
            conf.addRule( rule );
        }

        conf.initialise();

        return conf;
    }

    private static class Redirects
        implements Iterable<Redirect>
    {
        private final List<Redirect> redirects;

        private Redirects( final Builder builder )
        {
            this.redirects = builder.redirects;
        }

        @Override
        public Iterator<Redirect> iterator()
        {
            return this.redirects.iterator();
        }

        public static Builder create()
        {
            return new Builder();
        }


        public static final class Builder
        {
            private List<Redirect> redirects = Lists.newArrayList();

            private Builder redirect( final Redirect redirect )
            {
                this.redirects.add( redirect );
                return this;
            }

            private Redirects build()
            {
                return new Redirects( this );
            }
        }
    }

    private class Redirect
    {
        private final String from;

        private final String to;

        private Redirect( final String from, final String to )
        {
            this.from = from;
            this.to = to;
        }
    }
}