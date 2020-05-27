package com.enonic.app.rewrite.provider;


import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;

public interface RewriteMappingProvider
{
    RewriteMapping getRewriteMapping( final RewriteContextKey contextKey );

    void store( final RewriteMapping rewriteMapping );

    void create( final RewriteContextKey rewriteContext );

    void delete( final RewriteContextKey rewriteContextKey );

    void addRule( final RewriteContextKey key, final RewriteRule rule );

    boolean readOnly();

    String name();

}
