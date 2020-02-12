package com.enonic.app.rewrite.domain;

public class RewriteTarget
{
    private String target;

    boolean isAbsolute()
    {
        return this.target.startsWith( "/" );
    }

    String target()
    {
        return this.target;
    }

    public RewriteTarget( final String url )
    {

        this.target = url;
    }

    public static RewriteTarget from( final String value )
    {
        return new RewriteTarget( value );
    }

    @Override
    public String toString()
    {
        return this.target;
    }
}
