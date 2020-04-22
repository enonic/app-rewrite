package com.enonic.app.rewrite.requesttester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.google.common.collect.Maps;

class RewriteTesterRequest
    implements HttpServletRequest
{
    private final URL requestURL;

    private final URI requestURI;

    private final Map<String, Object> attributes = Maps.newHashMap();

    public RewriteTesterRequest( final String requestURL )
    {
        final String value = sanitize( requestURL );

        try
        {
            this.requestURL = new URL( value );
            this.requestURI = this.requestURL.toURI();
        }
        catch ( MalformedURLException | URISyntaxException e )
        {
            throw new IllegalArgumentException( "Cannot create test-request from [" + requestURL + "]", e );
        }

    }

    private String sanitize( final String value )
    {
        String sanitized = value;

        if ( !sanitized.startsWith( "https://" ) && !sanitized.startsWith( "http://" ) )
        {
            sanitized = "https://" + sanitized;
        }

        return sanitized;
    }

    @Override
    public String getAuthType()
    {
        return null;
    }

    @Override
    public Cookie[] getCookies()
    {
        return new Cookie[0];
    }

    @Override
    public long getDateHeader( final String s )
    {
        return 0;
    }

    @Override
    public String getHeader( final String s )
    {
        return null;
    }

    @Override
    public Enumeration<String> getHeaders( final String s )
    {
        return null;
    }

    @Override
    public Enumeration<String> getHeaderNames()
    {
        return null;
    }

    @Override
    public int getIntHeader( final String s )
    {
        return 0;
    }

    @Override
    public String getMethod()
    {
        return null;
    }

    @Override
    public String getPathInfo()
    {
        return null;
    }

    @Override
    public String getPathTranslated()
    {
        return null;
    }

    @Override
    public String getContextPath()
    {
        return null;
    }

    @Override
    public String getQueryString()
    {
        return null;
    }

    @Override
    public String getRemoteUser()
    {
        return null;
    }

    @Override
    public boolean isUserInRole( final String s )
    {
        return false;
    }

    @Override
    public Principal getUserPrincipal()
    {
        return null;
    }

    @Override
    public String getRequestedSessionId()
    {
        return null;
    }

    @Override
    public String getRequestURI()
    {
        return this.requestURI.getPath();
    }

    @Override
    public StringBuffer getRequestURL()
    {
        return null;
    }

    @Override
    public String getServletPath()
    {
        return null;
    }

    @Override
    public HttpSession getSession( final boolean b )
    {
        return null;
    }

    @Override
    public HttpSession getSession()
    {
        return null;
    }

    @Override
    public String changeSessionId()
    {
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    @Override
    public boolean authenticate( final HttpServletResponse httpServletResponse )
        throws IOException, ServletException
    {
        return false;
    }

    @Override
    public void login( final String s, final String s1 )
        throws ServletException
    {

    }

    @Override
    public void logout()
        throws ServletException
    {

    }

    @Override
    public Collection<Part> getParts()
        throws IOException, ServletException
    {
        return null;
    }

    @Override
    public Part getPart( final String s )
        throws IOException, ServletException
    {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade( final Class<T> aClass )
        throws IOException, ServletException
    {
        return null;
    }

    @Override
    public Object getAttribute( final String s )
    {
        return this.attributes.get( s );
    }

    @Override
    public Enumeration<String> getAttributeNames()
    {
        return null;
    }

    @Override
    public String getCharacterEncoding()
    {
        return null;
    }

    @Override
    public void setCharacterEncoding( final String s )
        throws UnsupportedEncodingException
    {

    }

    @Override
    public int getContentLength()
    {
        return 0;
    }

    @Override
    public long getContentLengthLong()
    {
        return 0;
    }

    @Override
    public String getContentType()
    {
        return null;
    }

    @Override
    public ServletInputStream getInputStream()
        throws IOException
    {
        return null;
    }

    @Override
    public String getParameter( final String s )
    {
        return null;
    }

    @Override
    public Enumeration<String> getParameterNames()
    {
        return null;
    }

    @Override
    public String[] getParameterValues( final String s )
    {
        return new String[0];
    }

    @Override
    public Map<String, String[]> getParameterMap()
    {
        return null;
    }

    @Override
    public String getProtocol()
    {
        return this.requestURL.getProtocol();
    }

    @Override
    public String getScheme()
    {
        return this.requestURI.getScheme();
    }

    @Override
    public String getServerName()
    {
        return this.requestURL.getHost();
    }

    @Override
    public int getServerPort()
    {
        return this.requestURL.getPort();
    }

    @Override
    public BufferedReader getReader()
        throws IOException
    {
        return null;
    }

    @Override
    public String getRemoteAddr()
    {
        return null;
    }

    @Override
    public String getRemoteHost()
    {
        return null;
    }

    @Override
    public void setAttribute( final String s, final Object o )
    {
        this.attributes.put( s, o );
    }

    @Override
    public void removeAttribute( final String s )
    {
        this.attributes.remove( s );
    }

    @Override
    public Locale getLocale()
    {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales()
    {
        return null;
    }

    @Override
    public boolean isSecure()
    {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher( final String s )
    {
        return null;
    }

    @Override
    public String getRealPath( final String s )
    {
        return null;
    }

    @Override
    public int getRemotePort()
    {
        return 0;
    }

    @Override
    public String getLocalName()
    {
        return null;
    }

    @Override
    public String getLocalAddr()
    {
        return null;
    }

    @Override
    public int getLocalPort()
    {
        return 0;
    }

    @Override
    public ServletContext getServletContext()
    {
        return null;
    }

    @Override
    public AsyncContext startAsync()
        throws IllegalStateException
    {
        return null;
    }

    @Override
    public AsyncContext startAsync( final ServletRequest servletRequest, final ServletResponse servletResponse )
        throws IllegalStateException
    {
        return null;
    }

    @Override
    public boolean isAsyncStarted()
    {
        return false;
    }

    @Override
    public boolean isAsyncSupported()
    {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext()
    {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType()
    {
        return null;
    }
}
