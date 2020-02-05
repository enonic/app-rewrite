package com.enonic.app.rewrite.provider;

import org.tuckey.web.filters.urlrewrite.Conf;

public interface RewriteRulesProvider
{
    Conf get();
}
