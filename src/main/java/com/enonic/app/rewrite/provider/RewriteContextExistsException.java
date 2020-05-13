package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.rewrite.RewriteContextKey;

public class RewriteContextExistsException
    extends RuntimeException
{

    public RewriteContextExistsException( final RewriteContextKey contextKey )
    {
        super( "Rewrite-context with key [" + contextKey + "] already exists" );
    }
}
