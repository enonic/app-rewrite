package com.enonic.app.rewrite.provider;


import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;

public interface RewriteMappingProvider
{
    RewriteMapping getAll();

    void store( final RewriteContextKey contextKey, final RewriteRule rule );

}
