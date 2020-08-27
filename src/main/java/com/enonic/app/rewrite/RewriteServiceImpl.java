package com.enonic.app.rewrite;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

import com.enonic.app.rewrite.engine.RewriteEngine;
import com.enonic.app.rewrite.engine.RewriteRulesLoadResult;
import com.enonic.app.rewrite.filter.RewriteFilterConfig;
import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.app.rewrite.provider.RewriteContextExistsException;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.provider.RewriteRulesProviderFactory;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
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

    private Cache<RewriteContextKey, RewriteMappingProvider> contextProviders;

    private Cache<RewriteContextKey, RewriteMapping> rewriteMappings;

    @Activate
    public void activate( final Map<String, String> map )
    {
        this.providers = this.rewriteRulesProviderFactory.getProviders( this.config );
        this.rewriteEngine = new RewriteEngine();
        this.contextProviders = CacheBuilder.newBuilder().build();
        this.rewriteMappings = CacheBuilder.newBuilder().build();
        doReload();
        LOG.info( "RewriteService initialized" );
    }

    @Override
    public RewriteRulesLoadResult load()
    {
        return doReload();
    }

    private RewriteRulesLoadResult doReload()
    {
        contextProviders.invalidateAll();
        rewriteMappings.invalidateAll();
        contextProviders.putAll( getVHostProviderConfig( this.vHostService.getMappings() ) );
        rewriteMappings.putAll( doGetRewriteMappings() );
        return this.rewriteEngine.load( RewriteMappings.from( this.rewriteMappings.asMap().values() ) );
    }

    @Override
    public RedirectMatch process( final HttpServletRequest request )
    {
        return this.rewriteEngine.process( request );
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey key )
    {
        return this.rewriteMappings.getIfPresent( key );
    }

    @Override
    public ConcurrentMap<RewriteContextKey, RewriteMappingProvider> getRewriteConfigurations()
    {
        return this.contextProviders.asMap();
    }

    @Override
    public void createRule( final CreateRuleParams params )
    {
        this.doGetProvider( params.getContextKey() ).createRule( params );
        doReload();
    }

    @Override
    public void deleteRule( final DeleteRuleParams params )
    {
        this.doGetProvider( params.getContextKey() ).deleteRule( params );
        doReload();
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
        final RewriteMappingProvider provider = this.doGetProvider( rewriteContextKey );

        if ( provider == null )
        {
            throw new IllegalArgumentException( "Provider with contextKey [" + rewriteContextKey + "] not found" );
        }

        provider.delete( rewriteContextKey );
        doReload();
    }

    @Override
    public ProviderInfo getProviderInfo( final RewriteContextKey rewriteContextKey )
    {
        final RewriteMappingProvider rewriteMappingProvider = this.doGetProvider( rewriteContextKey );
        return ProviderInfo.create().
            name( rewriteMappingProvider.name() ).
            readOnly( rewriteMappingProvider.readOnly() ).
            build();
    }

    @Override
    public void store( final RewriteMapping rewriteMapping )
    {
        final RewriteContextKey contextKey = rewriteMapping.getContextKey();
        final RewriteMappingProvider provider = doGetProvider( contextKey );
        provider.store( rewriteMapping );
        doReload();
    }

    public VirtualHostMapping getRewriteContext( final RewriteContextKey contextKey )
    {
        return this.vHostService.getMapping( contextKey.toString() );
    }

    private Map<RewriteContextKey, RewriteMapping> doGetRewriteMappings()
    {
        final Map<RewriteContextKey, RewriteMapping> map = Maps.newHashMap();

        LOG.info( "Finding rewrite-mappings for configurations" );

        this.contextProviders.asMap().forEach( ( context, provider ) -> {
            if ( provider != null )
            {
                LOG.info( "Loading rewrites for contextKey [{}] from provider [{}]", context, provider.name() );
                map.put( context, provider.getRewriteMapping( context ) );
            }
        } );

        return map;
    }

    private Map<RewriteContextKey, RewriteMappingProvider> getVHostProviderConfig( final VirtualHostMappings mappings )
    {
        final Map<RewriteContextKey, RewriteMappingProvider> providerMap = Maps.newHashMap();

        LOG.info( "Finding rewrite-configurations for vhosts" );
        mappings.forEach( vhost -> {
            final RewriteContextKey contextKey = new RewriteContextKey( vhost.getName() );
            final RewriteMappingProvider provider = fetchContextProvider( contextKey );
            providerMap.put( contextKey, provider );
        } );

        return providerMap;
    }

    private RewriteMappingProvider fetchContextProvider( final RewriteContextKey contextKey )
    {
        for ( final RewriteMappingProvider provider : providers )
        {
            final boolean providesForContext = provider.providesForContext( contextKey );
            LOG.info( "Check if provider [{}] provides for contextKey [{}] = [{}]", provider.name(), contextKey, providesForContext );
            if ( providesForContext )
            {
                LOG.info( "Found rewriteMapping provider [{}] for contextKey [{}]", provider.name(), contextKey );
                return provider;
            }
        }

        return null;
    }

    private RewriteMappingProvider doGetProvider( final RewriteContextKey contextKey )
    {
        return this.contextProviders.getIfPresent( contextKey );
    }

    @Reference
    @SuppressWarnings("unused")
    public void setVHostService( final VHostService vHostService )
    {
        this.vHostService = vHostService;
    }

    @Reference
    @SuppressWarnings("unused")
    public void setConfig( final RewriteFilterConfig config )
    {
        this.config = config;
    }

    @Reference
    @SuppressWarnings("unused")
    public void setRewriteRulesProviderFactory( final RewriteRulesProviderFactory rewriteRulesProviderFactory )
    {
        this.rewriteRulesProviderFactory = rewriteRulesProviderFactory;
    }
}



