package com.enonic.app.rewrite;

import com.enonic.app.rewrite.format.SourceFormat;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;

public class ExportRulesParams
{
    private RewriteContextKey contextKey;

    private SourceFormat format;

    public RewriteContextKey getContextKey()
    {
        return contextKey;
    }

    public SourceFormat getFormat()
    {
        return format;
    }

    public void setContextKey( final String contextKey )
    {
        this.contextKey = RewriteContextKey.from( contextKey );
    }

    public void setFormat( final String format )
    {
        this.format = SourceFormat.valueOf( format.toUpperCase() );
    }
}
