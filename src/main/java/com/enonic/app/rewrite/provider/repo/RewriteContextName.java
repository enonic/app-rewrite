package com.enonic.app.rewrite.provider.repo;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class RewriteContextName
{
    private final String name;

    private RewriteContextName( final String name )
    {
        this.name = name;
    }

    public static RewriteContextName from( final RewriteContextKey rewriteContextKey )
    {
        return new RewriteContextName( rewriteContextKey.toString() );
    }

    public String getName()
    {
        return name;
    }
}
