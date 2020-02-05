package com.enonic.app.rewrite;

import java.util.List;

import org.tuckey.web.filters.urlrewrite.Conf;

public interface RewriteFilterConfig
{
    boolean enabled();

    List<String> excludePatterns();

    List<String> includePatterns();

    Conf get();

    String configFile();
}
