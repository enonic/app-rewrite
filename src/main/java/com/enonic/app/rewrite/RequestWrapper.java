package com.enonic.app.rewrite;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper
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
