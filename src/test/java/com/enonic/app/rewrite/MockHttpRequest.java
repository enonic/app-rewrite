package com.enonic.app.rewrite;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;

import org.mockito.Mockito;

import com.enonic.xp.web.vhost.VirtualHost;

public class MockHttpRequest
{
    private final HttpServletRequest request;

    private MockHttpRequest( final Builder builder )
    {
        this.request = Mockito.mock( HttpServletRequest.class );
        Mockito.when( this.request.getContextPath() ).thenReturn( builder.contextPath );
        final URI uri = URI.create( builder.url );
        Mockito.when( this.request.getRequestURI() ).thenReturn( uri.getPath() );
        Mockito.when( this.request.getAttribute( VirtualHost.class.getName() ) ).thenReturn( builder.virtualHost );
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

        private VirtualHost virtualHost;

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

        public Builder vhost( final VirtualHost virtualHost )
        {
            this.virtualHost = virtualHost;
            return this;
        }

        public MockHttpRequest build()
        {
            return new MockHttpRequest( this );
        }

    }

}
