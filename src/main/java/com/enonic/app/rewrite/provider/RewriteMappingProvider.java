package com.enonic.app.rewrite.provider;


import com.enonic.app.rewrite.rewrite.RewriteContext;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;

public interface RewriteMappingProvider
{
    RewriteMappings getRewriteMappings();

    void store( final RewriteMapping rewriteMapping );

    void create( final RewriteContextKey rewriteContext );

    void delete( final RewriteContextKey rewriteContextKey );
}
