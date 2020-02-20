package com.enonic.app.rewrite.provider;

import java.nio.file.Paths;

import org.osgi.service.component.annotations.Component;

import com.enonic.app.rewrite.RewriteFilterConfig;
import com.enonic.xp.home.HomeDir;

@Component(immediate = true)
public class RewriteRulesProviderFactoryImpl
    implements RewriteRulesProviderFactory
{


    public RewriteRulesProvider get( final RewriteFilterConfig config )
    {
        final String rulePattern = config.ruleFilePattern();
        final HomeDir xpHome = HomeDir.get();
        return new RewriteRulesLocalFileProvider( Paths.get( xpHome.toFile().getPath(), "config" ), rulePattern );
    }


}
