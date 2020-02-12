package com.enonic.app.rewrite.provider;

import com.enonic.app.rewrite.RewriteFilterConfig;

public class RewriteRulesProviderFactoryImpl
    implements RewriteRulesProviderFactory
{
    public RewriteRulesProvider get( final RewriteFilterConfig config )
    {
        if ( true )
        {
            throw new RuntimeException( "HANDLE THE BAS EPATH" );
        }

        return new RewriteRulesLocalFileProvider( null, config.ruleFilePattern() );
    }

}
