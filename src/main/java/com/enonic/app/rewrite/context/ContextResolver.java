package com.enonic.app.rewrite.context;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextResolver
{
    private final static Logger LOG = LoggerFactory.getLogger( ContextResolver.class );

    public static String resolve( final HttpServletRequest request )
    {
        LOG.debug( "Resolving context for [{}], currentContext is [{}]", request.getRequestURI(), request.getContextPath() );
        return request.getContextPath();
    }


}
