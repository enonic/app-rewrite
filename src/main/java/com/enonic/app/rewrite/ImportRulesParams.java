package com.enonic.app.rewrite;

import com.google.common.io.ByteSource;

import com.enonic.app.rewrite.rewrite.RewriteContextKey;

public class ImportRulesParams
{

    private String mergeStrategy;

    private boolean dryRun;

    private ByteSource byteSource;

    private RewriteContextKey contextKey;

    public void setContextKey( final String contextKey )
    {
        this.contextKey = RewriteContextKey.from( contextKey );
    }

    public void setMergeStrategy( final String mergeStrategy )
    {
        this.mergeStrategy = mergeStrategy;
    }

    public void setDryRun( final boolean dryRun )
    {
        this.dryRun = dryRun;
    }

    public void setByteSource( final ByteSource byteSource )
    {
        this.byteSource = byteSource;
    }

    public RewriteContextKey getContextKey()
    {
        return contextKey;
    }

    public String getMergeStrategy()
    {
        return mergeStrategy;
    }

    public boolean isDryRun()
    {
        return dryRun;
    }

    public ByteSource getByteSource()
    {
        return byteSource;
    }
}