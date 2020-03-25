package com.enonic.app.rewrite;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.domain.RedirectMatch;
import com.enonic.app.rewrite.domain.RewriteMapping;

public interface RewriteService
{
    RedirectMatch process( final HttpServletRequest request );

    RewriteMapping getRewriteMapping();

}
