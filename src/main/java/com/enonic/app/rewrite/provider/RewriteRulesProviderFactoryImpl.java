package com.enonic.app.rewrite.provider;

import java.nio.file.Paths;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.RewriteFilterConfig;
import com.enonic.app.rewrite.vhost.VirtualHostResolver;
import com.enonic.xp.home.HomeDir;

@Component(immediate = true)
public class RewriteRulesProviderFactoryImpl
    implements RewriteRulesProviderFactory
{
    private VirtualHostResolver virtualHostResolver;

    public RewriteRulesProvider get( final RewriteFilterConfig config )
    {
        final String rulePattern = config.ruleFilePattern();
        final HomeDir xpHome = HomeDir.get();
        return new RewriteRulesLocalFileProvider( Paths.get( xpHome.toFile().getPath(), "config" ), rulePattern, this.virtualHostResolver );
    }

    @Reference
    public void setVirtualHostResolver( final VirtualHostResolver virtualHostResolver )
    {
        this.virtualHostResolver = virtualHostResolver;
    }
}
