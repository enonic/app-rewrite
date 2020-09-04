package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class RewriteContextNotFoundException
    extends RuntimeException
{
    public RewriteContextNotFoundException( final RewriteContextKey key )
    {
        super( "Rewrite with key " + key + " not founds" );
    }
}
