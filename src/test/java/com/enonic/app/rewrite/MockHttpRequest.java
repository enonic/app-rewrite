package com.enonic.app.rewrite;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;

public class MockHttpRequest
{

    public MockHttpRequest( final HttpServletRequest request, final String contextPath )
    {
        this.request = request;
        this.contextPath = contextPath;
    }

    private final HttpServletRequest request;

    private final String contextPath;

    private MockHttpRequest( final Builder builder )
    {
        contextPath = builder.contextPath;

        this.request = Mockito.mock( HttpServletRequest.class );
        final URL url;
        try
        {
            url = new URL( builder.url );
        }
        catch ( MalformedURLException e )
        {
            throw new RuntimeException( "Cannot create url" );
        }

        Mockito.when( request.getProtocol() ).thenReturn( url.getProtocol() );
        Mockito.when( request.getServerName() ).thenReturn( url.getHost() );
        Mockito.when( request.getServerPort() ).thenReturn( url.getPort() );
        Mockito.when( request.getQueryString() ).thenReturn( url.getQuery() );
        Mockito.when( request.getRequestURI() ).thenReturn( url.getPath() );
        Mockito.when( request.getContextPath() ).thenReturn( contextPath );
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public static Builder create()
    {
        return new Builder();
    }


    public static final class Builder
    {
        private String url;

        private String contextPath;

        private Builder()
        {
        }

        public Builder url( final String url )
        {
            this.url = url;
            return this;
        }

        public Builder contextPath( final String contextPath )
        {
            this.contextPath = contextPath;
            return this;
        }

        public MockHttpRequest build()
        {
            return new MockHttpRequest( this );
        }
    }

}
