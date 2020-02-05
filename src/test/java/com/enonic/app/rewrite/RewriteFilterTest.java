package com.enonic.app.rewrite;


import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.RewrittenUrl;
import org.tuckey.web.filters.urlrewrite.UrlRewriter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RewriteFilterTest
{
    private UrlRewriter urlRewriter;


    @BeforeAll
    public static void setUp()
    {

    }

    @Test
    void name()
        throws Exception
    {
        final Conf conf = RewriteTestHelper.getConf( "test1.xml" );
        urlRewriter = new UrlRewriter( conf );
        assertEquals( urlRewriter.getConf().getRules().size(), 1 );
        final HttpServletResponse response = Mockito.mock( HttpServletResponse.class );

        final RewrittenUrl rewrittenUrl = urlRewriter.processRequest( MockHttpRequest.create().
            url( "https://rett24.no/portal/master/rett24/ost/fisk/polse" ).
            contextPath( "/portal/master/rett24" ).
            build().
            getRequest(), response );

        if ( rewrittenUrl != null )
        {
            System.out.println( rewrittenUrl.getTarget() );
        }
    }


}
