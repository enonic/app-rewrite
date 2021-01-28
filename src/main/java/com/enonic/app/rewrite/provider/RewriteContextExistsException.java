package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class RewriteContextExistsException
    extends RewriteRuleException
{
    public RewriteContextExistsException( final RewriteContextKey contextKey )
    {
        super( "Virtual host " + contextKey + " already exists" );
    }
}
