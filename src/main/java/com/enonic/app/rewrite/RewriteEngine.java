package com.enonic.app.rewrite;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface RewriteEngine
{
    String process( final HttpServletRequest request, final HttpServletResponse response );

    boolean process( final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain );

}
