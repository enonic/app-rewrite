package com.enonic.app.rewrite.domain;

public class RewriteTarget
{
    private final String path;

    private final boolean isExternal;

    private RewriteTarget( final String path )
    {
        this.path = path;
        this.isExternal = path.startsWith( "http" );
    }

    public static RewriteTarget from( final String targetString )
    {
        return new RewriteTarget( targetString );
    }

    public String path()
    {
        return path;
    }

    public boolean isExternal()
    {
        return isExternal;
    }
}


