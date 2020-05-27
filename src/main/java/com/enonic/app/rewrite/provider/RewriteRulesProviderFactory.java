package com.enonic.app.rewrite.provider;

import java.util.List;

import com.enonic.app.rewrite.filter.RewriteFilterConfig;

public interface RewriteRulesProviderFactory
{
    List<RewriteMappingProvider> getProviders( final RewriteFilterConfig config );
}
