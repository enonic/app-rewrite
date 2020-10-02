package com.enonic.app.rewrite.filter;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.enonic.app.rewrite.RewriteConfig;

@Component(enabled = false, configurationPid = "com.enonic.app.rewrite")
public class RewriteFilterConfigImpl
    implements RewriteFilterConfig
{
    private RewriteConfig rewriteConfig;

    @Activate
    public void activate( final RewriteConfig rewriteConfig )
    {
        this.rewriteConfig = rewriteConfig;
    }

    @Override
    public boolean enabled()
    {
        return rewriteConfig.enabled();
    }

    @Override
    public String excludePattern()
    {
        return rewriteConfig.excludePattern();
    }

    @Override
    public String includePattern()
    {
        return rewriteConfig.includePattern();
    }

    @Override
    public String ruleFileNameTemplate()
    {
        return rewriteConfig.ruleFileNameTemplate();
    }

}
