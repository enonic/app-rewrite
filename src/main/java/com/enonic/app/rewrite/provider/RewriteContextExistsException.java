package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class RewriteContextExistsException
    extends RewriteRuleException
{
    public RewriteContextExistsException( final RewriteContextKey contextKey )
    {
        super( "Rewrite-context with key [" + contextKey + "] already exists" );
    }
}
