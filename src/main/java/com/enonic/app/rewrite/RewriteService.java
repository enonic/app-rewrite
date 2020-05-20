package com.enonic.app.rewrite;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;

public interface RewriteService
{
    RedirectMatch process( final HttpServletRequest request );

    RewriteMappings getRewriteMappings();

    void store( final RewriteMapping rewriteMapping );

    void addRule( final RewriteContextKey rewriteContextKey, final RewriteRule rule );

    void create( final RewriteContextKey rewriteContextKey );

    void delete( final RewriteContextKey rewriteContextKey );

    ProviderInfo getProviderInfo();

}
