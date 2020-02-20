package com.enonic.app.rewrite.domain;

public interface RewriteContext
{
    RewriteContextKey getKey();

    String getSourceContext();

    String getTargetContext();
}
