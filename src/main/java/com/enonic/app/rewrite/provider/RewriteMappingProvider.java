package com.enonic.app.rewrite.provider;


import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;

public interface RewriteMappingProvider
{
    RewriteMappings getAll();

    void store( final RewriteMapping rewriteMapping );

}
