package com.enonic.app.rewrite;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.app.rewrite.domain.RewriteMapping;

public interface RewriteService
{

    Redirect process( final HttpServletRequest request );

    RewriteMapping getRewriteMapping();

}
