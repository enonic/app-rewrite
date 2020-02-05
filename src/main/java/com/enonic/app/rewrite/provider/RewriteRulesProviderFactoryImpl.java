package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.RewriteFilterConfig;

public class RewriteRulesProviderFactoryImpl
    implements RewriteRulesProviderFactory
{
    public RewriteRulesProvider get( final RewriteFilterConfig config )
    {
        return new RewriteRulesLocalFileProvider( config.configFile() );
    }

}
