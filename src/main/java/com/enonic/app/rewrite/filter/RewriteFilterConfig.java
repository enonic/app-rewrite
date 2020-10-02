package com.enonic.app.rewrite.filter;

public interface RewriteFilterConfig
{
    boolean enabled();

    String excludePattern();

    String includePattern();

    String ruleFileNameTemplate();
}
