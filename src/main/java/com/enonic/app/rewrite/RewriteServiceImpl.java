package com.enonic.app.rewrite;

import java.util.List;
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
import com.enonic.app.rewrite.provider.RewriteContextExistsException;
import com.enonic.app.rewrite.provider.RewriteContextNotFoundException;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.provider.RewriteRulesProviderFactory;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.vhost.RewriteConfigurations;
import com.enonic.app.rewrite.vhost.VHostService;
import com.enonic.app.rewrite.vhost.VirtualHostMapping;
import com.enonic.app.rewrite.vhost.VirtualHostMappings;

@Component(immediate = true)
public class RewriteServiceImpl
    implements RewriteService
{
    private RewriteEngine rewriteEngine;

    private RewriteRulesProviderFactory rewriteRulesProviderFactory;

    private RewriteFilterConfig config;

    private final static Logger LOG = LoggerFactory.getLogger( RewriteServiceImpl.class );

    private VHostService vHostService;

    private List<RewriteMappingProvider> providers;

    @Activate
    public void activate( final Map<String, String> map )
    {
        this.providers = this.rewriteRulesProviderFactory.getProviders( this.config );
        this.rewriteEngine = new RewriteEngine();
        LOG.info( "RewriteService initialized" );
        doLoadRewriteMappings();
    }

    @Override
    public RewriteRulesLoadResult load()
    {
        return doLoadRewriteMappings();
    }

    private RewriteRulesLoadResult doLoadRewriteMappings()
    {
        return this.rewriteEngine.load( doGetRewriteMappings() );
    }


    private RewriteMappings doGetRewriteMappings()
    {
        final VirtualHostMappings mappings = this.vHostService.getMappings();
        final RewriteConfigurations rewriteConfigurations = doGetRewriteConfigurations( mappings );
        return doGetRewriteMappings( rewriteConfigurations );
    }

    private RewriteMappings doGetRewriteMappings( final RewriteConfigurations rewriteConfigurations )
    {
        final RewriteMappings.Builder builder = RewriteMappings.create();
        final Map<RewriteContextKey, RewriteMappingProvider> configs = rewriteConfigurations.getConfigurations();
        configs.forEach( ( context, provider ) -> {
            if ( provider != null )
            {
                builder.add( provider.getRewriteMapping( context ) );
            }
        } );

        return builder.build();
    }

    private RewriteConfigurations doGetRewriteConfigurations( final VirtualHostMappings mappings )
    {
        final RewriteConfigurations.Builder configBuilder = RewriteConfigurations.create();

        mappings.forEach( vhost -> {

            final RewriteContextKey contextKey = new RewriteContextKey( vhost.getName() );
            configBuilder.add( contextKey, null );

            for ( final RewriteMappingProvider provider : providers )
            {
                final RewriteMapping mapping = provider.getRewriteMapping( contextKey );
                if ( mapping != null )
                {
                    LOG.info( "Found rewriteMapping in provider [" + contextKey + " - " + provider.name() + "]" );
                    configBuilder.add( contextKey, provider );
                    break;
                }
            }
        } );

        return configBuilder.build();
    }

    public List<RewriteMappingProvider> getProviders()
    {
        return providers;
    }

    public VirtualHostMapping getRewriteContext( final RewriteContextKey contextKey )
    {
        return this.vHostService.getMapping( contextKey.toString() );
    }

    @Override
    public RedirectMatch process( final HttpServletRequest request )
    {
        return this.rewriteEngine.process( request );
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey key )
    {
        final RewriteMappingProvider provider = doGetProvider( key );

        if ( provider == null )
        {
            return null;
        }

        return provider.getRewriteMapping( key );
    }

    @Override
    public RewriteConfigurations getRewriteConfigurations()
    {
        return doGetRewriteConfigurations( this.vHostService.getMappings() );
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

    public void store( final RewriteMapping rewriteMapping )
    {
        final RewriteContextKey contextKey = rewriteMapping.getContextKey();
        final RewriteMappingProvider provider = doGetProvider( contextKey );
        provider.store( rewriteMapping );
        doLoadRewriteMappings();
    }

    private RewriteMappingProvider doGetProvider( final RewriteContextKey contextKey )
    {
        final VirtualHostMappings mappings = this.vHostService.getMappings();
        final RewriteConfigurations rewriteConfigurations = doGetRewriteConfigurations( mappings );

        final RewriteMappingProvider provider = rewriteConfigurations.getProvider( contextKey );

        if ( provider == null )
        {
            return null;
        }
        return provider;
    }

    @Override
    public void addRule( final RewriteContextKey rewriteContextKey, final RewriteRule rule )
    {
        this.doGetProvider( rewriteContextKey ).addRule( rewriteContextKey, rule );
        doLoadRewriteMappings();
    }

    @Override
    public void createRule( final CreateRuleParams params )
    {
        this.doGetProvider( RewriteContextKey.from( params.getContextKey()) ).createRule( rewriteContextKey, rule );
    }

    @Override
    public void create( final RewriteContextKey rewriteContextKey )
    {
        final RewriteMappingProvider existingProvider = doGetProvider( rewriteContextKey );

        if ( existingProvider != null )
        {
            throw new RewriteContextExistsException( rewriteContextKey );
        }

        for ( final RewriteMappingProvider provider : this.providers )
        {
            if ( !provider.readOnly() )
            {
                provider.create( rewriteContextKey );
                return;
            }
        }

        throw new IllegalArgumentException( "No provider with storage capability found, context not stored" );
    }

    @Override
    public void delete( final RewriteContextKey rewriteContextKey )
    {
        this.doGetProvider( rewriteContextKey ).delete( rewriteContextKey );
    }

    @Reference
    public void setvHostService( final VHostService vHostService )
    {
        this.vHostService = vHostService;
    }
}



