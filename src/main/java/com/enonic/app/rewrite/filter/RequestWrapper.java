package com.enonic.app.rewrite.filter;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.enonic.app.rewrite.URLPathDecoder;

class RequestWrapper
    extends HttpServletRequestWrapper
{
    private String contextPath;

    RequestWrapper( final HttpServletRequest request )
    {
        super( request );
    }

    void setContextPath( final String contextPath )
    {
        this.contextPath = contextPath;
    }

    @Override
    public String getRequestURI()
    {
        return URLPathDecoder.decode( super.getRequestURI(), StandardCharsets.UTF_8 );
    }

    @Override
    public String getContextPath()
    {
        return this.contextPath != null ? this.contextPath : super.getContextPath();
    }
}
