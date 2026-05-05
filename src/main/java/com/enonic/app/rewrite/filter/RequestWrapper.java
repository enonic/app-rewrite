package com.enonic.app.rewrite.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

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
    public String getContextPath()
    {
        return this.contextPath != null ? this.contextPath : super.getContextPath();
    }
}
