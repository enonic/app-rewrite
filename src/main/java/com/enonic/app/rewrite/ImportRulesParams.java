package com.enonic.app.rewrite;

import com.google.common.io.ByteSource;

import com.enonic.app.rewrite.domain.RewriteContextKey;

public class ImportRulesParams
{
    private String mergeStrategy;

    private boolean dryRun;

    private ByteSource byteSource;

    private RewriteContextKey contextKey;

    private String fileName;

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

    public void setContextKey( final RewriteContextKey contextKey )
    {
        this.contextKey = contextKey;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( final String fileName )
    {
        this.fileName = fileName;
    }
}