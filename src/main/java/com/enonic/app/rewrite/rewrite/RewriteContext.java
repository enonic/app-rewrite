package com.enonic.app.rewrite.rewrite;

public interface RewriteContext
{
    RewriteContextKey getKey();

    String getSourceContext();

    String getTargetContext();
}
