package com.enonic.app.rewrite.engine;

import javax.servlet.http.HttpServletRequest;

public interface RewriteEngine
{
    String process( final HttpServletRequest request );
}
