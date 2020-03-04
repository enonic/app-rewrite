package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.filter.RewriteFilterConfig;

public interface RewriteRulesProviderFactory
{
    RewriteMappingProvider get( final RewriteFilterConfig config );
}
