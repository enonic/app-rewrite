package com.enonic.app.rewrite;

import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;

public class RewriteConfiguration
{
    private final RewriteContextKey contextKey;

    private final RewriteMappingProvider provider;

    public RewriteConfiguration( final RewriteContextKey contextKey, final RewriteMappingProvider provider )
    {
        this.contextKey = contextKey;
        this.provider = provider;
    }



}
