package com.enonic.app.rewrite;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.app.rewrite.domain.RedirectMatch;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.engine.RewriteEngine;
import com.enonic.app.rewrite.filter.RewriteFilterConfig;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.provider.RewriteRulesProviderFactory;

@Component(immediate = true)
public class RewriteServiceImpl
    implements RewriteService
{
    private RewriteEngine rewriteEngine;

    private RewriteRulesProviderFactory rewriteRulesProviderFactory;

    private RewriteMapping rewriteMapping;

    private RewriteFilterConfig config;

    @Activate
    public void activate( final Map<String, String> map )
    {
        final RewriteMappingProvider rewriteMappingProvider = this.rewriteRulesProviderFactory.get( this.config );
        this.rewriteMapping = rewriteMappingProvider.get();
        this.rewriteEngine = new RewriteEngine( rewriteMapping );
    }

    @Override
    public RedirectMatch process( final HttpServletRequest request )
    {
        return this.rewriteEngine.process( request );
    }

    public RewriteMapping getRewriteMapping()
    {
        return this.rewriteMapping;
    }

    @Reference
    public void setConfig( final RewriteFilterConfig config )
    {
        this.config = config;
    }

    @Reference
    public void setRewriteRulesProviderFactory( final RewriteRulesProviderFactory rewriteRulesProviderFactory )
    {
        this.rewriteRulesProviderFactory = rewriteRulesProviderFactory;
    }
}



