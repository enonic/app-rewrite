package com.enonic.app.rewrite;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteMappings;
import com.enonic.app.rewrite.engine.RewriteEngine;
import com.enonic.app.rewrite.engine.RewriteRulesLoadResult;
import com.enonic.app.rewrite.filter.RewriteFilterConfig;
import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.app.rewrite.provider.RewriteContextExistsException;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.provider.file.RewriteMappingLocalFileProvider;
import com.enonic.app.rewrite.provider.repo.RewriteRepoMappingProvider;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.xp.home.HomeDir;
import com.enonic.xp.node.NodeService;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostService;

@Component(enabled = false)
public class RewriteServiceImpl
    implements RewriteService
{
    private static final Logger LOG = LoggerFactory.getLogger( RewriteServiceImpl.class );

    private RewriteEngine rewriteEngine;

    private RewriteFilterConfig config;

    private NodeService nodeService;

    private VirtualHostService virtualHostService;

    private List<RewriteMappingProvider> providers;

    private Cache<RewriteContextKey, Optional<RewriteMappingProvider>> contextProviders;

    private Cache<RewriteContextKey, RewriteMapping> rewriteMappings;

    @Activate
    public void activate()
    {
        this.providers = List.of( initFileProvider(), initRepoProvider() );

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

    @Override
    public RedirectMatch process( final HttpServletRequest request )
    {
        return this.rewriteEngine.process( request );
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey key )
    {
        if ( key == null )
        {
            LOG.info( "Key is null, this sucks" );
            return null;
        }

        return this.rewriteMappings.getIfPresent( key );
    }

    @Override
    public ConcurrentMap<RewriteContextKey, Optional<RewriteMappingProvider>> getRewriteConfigurations()
    {
        return this.contextProviders.asMap();
    }

    @Override
    public void createRule( final CreateRuleParams params )
    {
        getProviderOrThrow( params.getContextKey() ).createRule( params );

        doReload();
    }

    @Override
    public void deleteRule( final DeleteRuleParams params )
    {
        getProviderOrThrow( params.getContextKey() ).deleteRule( params );

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
                doReload();
                return;
            }
        }

        throw new IllegalArgumentException( "No provider with storage capability found, context not stored" );
    }

    @Override
    public void delete( final RewriteContextKey rewriteContextKey )
    {
        getProviderOrThrow( rewriteContextKey ).delete( rewriteContextKey );

        doReload();
    }

    @Override
    public void editRule( final EditRuleParams params )
    {
        getProviderOrThrow( params.getContextKey() ).editRule( params );

        doReload();
    }

    @Override
    public ProviderInfo getProviderInfo( final RewriteContextKey rewriteContextKey )
    {
        final RewriteMappingProvider rewriteMappingProvider = getProviderOrThrow( rewriteContextKey );

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

        if ( provider != null )
        {
            provider.store( rewriteMapping );
            doReload();
        }
    }

    @Override
    public VirtualHost getRewriteContext( final RewriteContextKey contextKey )
    {
        for ( final VirtualHost virtualHost : virtualHostService.getVirtualHosts() )
        {
            if ( virtualHost.getName().equals( contextKey.toString() ) )
            {
                return virtualHost;
            }
        }

        throw new IllegalArgumentException( "RewriteContext with contextKey [" + contextKey + "] not found" );
    }

    private RewriteRulesLoadResult doReload()
    {
        contextProviders.invalidateAll();
        rewriteMappings.invalidateAll();
        contextProviders.putAll( getVHostProviderConfig( this.virtualHostService.getVirtualHosts() ) );
        rewriteMappings.putAll( doGetRewriteMappings() );
        return this.rewriteEngine.load( RewriteMappings.from( this.rewriteMappings.asMap().values() ) );
    }

    private Map<RewriteContextKey, RewriteMapping> doGetRewriteMappings()
    {
        final Map<RewriteContextKey, RewriteMapping> map = new HashMap<>();

        LOG.info( "Finding rewrite-mappings for configurations" );

        this.contextProviders.asMap().forEach( ( context, provider ) -> {

            if ( provider.isPresent() )
            {
                LOG.info( "Loading rewrites for contextKey [{}] from provider [{}]", context, provider.get().name() );
                map.put( context, provider.get().getRewriteMapping( context ) );
            }
        } );

        return map;
    }

    private Map<RewriteContextKey, Optional<RewriteMappingProvider>> getVHostProviderConfig( final List<VirtualHost> mappings )
    {
        final Map<RewriteContextKey, Optional<RewriteMappingProvider>> providerMap = new HashMap<>();

        LOG.info( "Finding rewrite-configurations for vhosts" );
        mappings.forEach( vhost -> {
            final RewriteContextKey contextKey = new RewriteContextKey( vhost.getName() );
            final RewriteMappingProvider provider = fetchContextProvider( contextKey );
            providerMap.put( contextKey, Optional.ofNullable( provider ) );
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

    private RewriteMappingProvider doGetProvider( final RewriteContextKey rewriteContextKey )
    {
        return this.contextProviders.getIfPresent( rewriteContextKey ).orElse( null );
    }

    private RewriteMappingProvider getProviderOrThrow( final RewriteContextKey rewriteContextKey )
    {
        final RewriteMappingProvider provider = doGetProvider( rewriteContextKey );

        if ( provider == null )
        {
            throw new IllegalArgumentException( "Provider with contextKey [" + rewriteContextKey + "] not found" );
        }

        return provider;
    }

    private RewriteMappingProvider initFileProvider()
    {
        final HomeDir xpHome = HomeDir.get();
        return RewriteMappingLocalFileProvider.create().
            base( Paths.get( xpHome.toFile().getPath(), "config" ) ).
            ruleFileNameTemplate( config.ruleFileNameTemplate() ).
            build();
    }

    private RewriteMappingProvider initRepoProvider()
    {
        return new RewriteRepoMappingProvider( this.nodeService );
    }

    @Reference
    @SuppressWarnings("unused")
    public void setVHostService( final VirtualHostService virtualHostService )
    {
        this.virtualHostService = virtualHostService;
    }

    @Reference
    public void setConfig( final RewriteFilterConfig config )
    {
        this.config = config;
    }

    @Reference
    public void setNodeService( final NodeService nodeService )
    {
        this.nodeService = nodeService;
    }

}
