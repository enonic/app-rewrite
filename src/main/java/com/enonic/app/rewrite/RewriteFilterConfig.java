package com.enonic.app.rewrite;

import java.util.List;

public interface RewriteFilterConfig
{
    boolean enabled();

    List<String> excludePatterns();

    List<String> includePatterns();

    String ruleFilePattern();
}
