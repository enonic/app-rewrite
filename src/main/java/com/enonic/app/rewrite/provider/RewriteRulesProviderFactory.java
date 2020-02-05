package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.RewriteFilterConfig;

public interface RewriteRulesProviderFactory
{
    RewriteRulesProvider get( final RewriteFilterConfig config );
}
