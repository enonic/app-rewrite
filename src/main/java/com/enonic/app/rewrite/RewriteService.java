package com.enonic.app.rewrite;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;

public interface RewriteService
{
    RedirectMatch process( final HttpServletRequest request );

    RewriteMapping getRewriteMapping();

    void store( final RewriteContextKey contextKey, final RewriteRule rewriteRule );

}
