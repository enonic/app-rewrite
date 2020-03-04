package com.enonic.app.rewrite.filter;

import java.util.List;

public interface RewriteFilterConfig
{
    boolean enabled();

    List<String> excludePatterns();

    List<String> includePatterns();

    String ruleFilePattern();
}
