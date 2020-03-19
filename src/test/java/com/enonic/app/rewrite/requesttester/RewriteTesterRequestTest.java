package com.enonic.app.rewrite.requesttester;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RewriteTesterRequestTest
{

    @Test
    void testUrls()
    {

        assertEquals( "/fisk", createRequest( "http://myhost.com/fisk" ).getRequestURI() );
        assertEquals( "/fisk", createRequest( "myhost.com/fisk" ).getRequestURI() );

    }

    private RewriteTesterRequest createRequest( final String url )
    {
        return new RewriteTesterRequest( url );
    }

}