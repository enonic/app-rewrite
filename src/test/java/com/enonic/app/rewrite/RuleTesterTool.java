package com.enonic.app.rewrite;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.NormalRule;
import org.tuckey.web.filters.urlrewrite.RewrittenUrl;
import org.tuckey.web.filters.urlrewrite.Rule;

import com.google.common.collect.Lists;

public class RuleTesterTool
{

    @Test
    void testUrls()
    {

        final Conf confg = buildTestConf( Lists.newArrayList( new Redirect( "/a/b", "/c/d" ) ) );
        final NormalRule rule = (NormalRule) confg.getRules().get( 0 );

        testRuleForRequest( rule, "https://test.com/a/b", "/" );
        testRuleForRequest( rule, "https://test.com/a/b/c", "/" );
        testRuleForRequest( rule, "https://test.com/a", "/" );
    }

    private void testRuleForRequest( final Rule rule, final String url, final String context )
    {

        try
        {
            final MockHttpRequest request = MockHttpRequest.create().
                contextPath( context ).
                url( url ).
                build();

            final RewrittenUrl matches = rule.matches( url, request.getRequest(), Mockito.mock( HttpServletResponse.class ) );
            System.out.println( String.format( "Rule maching [%s] with context [%s]: %b", url, context, matches ) );
        }
        catch ( ServletException | IOException | InvocationTargetException e )
        {
            System.out.println( "Rule matching failed: " + e.getMessage() );
        }

    }

    private final Conf buildTestConf( final Collection<Redirect> redirects )
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
