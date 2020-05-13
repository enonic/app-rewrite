package com.enonic.app.rewrite;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;

public interface RewriteService
{
    RedirectMatch process( final HttpServletRequest request );

    RewriteMappings getRewriteMappings();

    void store( final RewriteMapping rewriteMapping );

    void create( final RewriteContextKey rewriteContextKey );

    void delete( final RewriteContextKey rewriteContextKey );

}
