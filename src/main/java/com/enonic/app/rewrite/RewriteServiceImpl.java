package com.enonic.app.rewrite;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.engine.RewriteEngine;
import com.enonic.app.rewrite.engine.RewriteRulesLoadResult;
import com.enonic.app.rewrite.filter.RewriteFilterConfig;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.provider.RewriteRulesProviderFactory;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;

@Component(immediate = true)
public class RewriteServiceImpl
    implements RewriteService
{
    private RewriteEngine rewriteEngine;

    private RewriteRulesProviderFactory rewriteRulesProviderFactory;

    private RewriteMapping rewriteMapping;

    private RewriteFilterConfig config;

    private RewriteMappingProvider provider;

    private final static Logger LOG = LoggerFactory.getLogger( RewriteServiceImpl.class );

    @Activate
    public void activate( final Map<String, String> map )
    {
        this.provider = this.rewriteRulesProviderFactory.get( this.config );
        this.rewriteMapping = this.provider.getAll();
        this.rewriteEngine = new RewriteEngine();
        LOG.info( "Loading rules into engine" );
        final RewriteRulesLoadResult loadResult = this.rewriteEngine.load( rewriteMapping );
        LOG.info( "Loaded rules: " + loadResult );
        LOG.info( "RewriteService initialized" );
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

    public void store( final RewriteContextKey contextKey, final RewriteRule rewriteRule )
    {
        this.provider.store( contextKey, rewriteRule );
    }
}



