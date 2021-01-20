package com.enonic.app.rewrite;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.engine.ExtRulePattern;
import com.enonic.app.rewrite.engine.RewriteEngine;
import com.enonic.app.rewrite.engine.RewriteRulesLoadResult;
import com.enonic.app.rewrite.filter.RewriteFilterConfig;
import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.app.rewrite.provider.RewriteContextExistsException;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.provider.file.FileNameMatcher;
import com.enonic.app.rewrite.provider.file.RewriteMappingLocalFileProvider;
import com.enonic.app.rewrite.provider.repo.RewriteMappingRepoInitializer;
import com.enonic.app.rewrite.provider.repo.RewriteRepoMappingProvider;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.xp.home.HomeDir;
import com.enonic.xp.node.FindNodesByQueryResult;
import com.enonic.xp.node.NodePath;
import com.enonic.xp.node.NodeQuery;
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

    private Cache<RewriteContextKey, VirtualHostWrapper> container;

    @Activate
    public void activate()
    {
        this.providers = List.of( initFileProvider(), initRepoProvider() );

        this.rewriteEngine = new RewriteEngine();
        this.contextProviders = CacheBuilder.newBuilder().build();
        this.rewriteMappings = CacheBuilder.newBuilder().build();

        this.container = CacheBuilder.newBuilder().build();

        doReload();
        LOG.info( "RewriteService initialized" );
    }

    @Override
    public VirtualHostsDecorator getVirtualHostMappings()
    {
        return new VirtualHostsDecorator( container.asMap() );
    }

    @Override
    public void reloadRewriteMappings()
    {
        doReload();
    }

    @Override
    public RedirectMatch process( final HttpServletRequest request )
    {
        return this.rewriteEngine.process( request );
    }

    @Override
    public RedirectMatch process( final HttpServletRequest request, final ExtRulePattern extRulePattern )
    {
        return rewriteEngine.process( request, extRulePattern );
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey key )
    {
        return key != null ? this.rewriteMappings.getIfPresent( key ) : null;
    }

    @Override
    public ConcurrentMap<RewriteContextKey, Optional<RewriteMappingProvider>> getRewriteConfigurations()
    {
        return this.contextProviders.asMap();
    }

    @Override
    public void saveRule( final UpdateRuleParams params )
    {
        if ( Strings.isNullOrEmpty( params.getSource() ) || Strings.isNullOrEmpty( params.getTarget() ) )
        {
            throw new IllegalArgumentException( "The fields \"Source path\" and \"Target path\" are mandatory." );
        }

        getProviderOrThrow( params.getContextKey() ).saveRule( params );

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

    private RewriteRulesLoadResult doReload()
    {
        contextProviders.invalidateAll();
        rewriteMappings.invalidateAll();
        container.invalidateAll();

        initVHostProviderConfig();

        rewriteMappings.putAll( doGetRewriteMappings() );
        return this.rewriteEngine.load( this.rewriteMappings.asMap().values() );
    }

    private Map<RewriteContextKey, RewriteMapping> doGetRewriteMappings()
    {
        final Map<RewriteContextKey, RewriteMapping> map = new HashMap<>();

        LOG.debug( "Finding rewrite-mappings for configurations" );

        this.contextProviders.asMap().forEach( ( context, provider ) -> {

            if ( provider.isPresent() )
            {
                LOG.debug( "Loading rewrites for contextKey [{}] from provider [{}]", context, provider.get().name() );
                map.put( context, provider.get().getRewriteMapping( context ) );
            }
        } );

        return map;
    }

    private void initVHostProviderConfig()
    {
        final Map<String, VirtualHost> virtualHostAsMap = virtualHostService.getVirtualHosts().stream().
            collect( Collectors.toMap( VirtualHost::getName, Function.identity() ) );

        final Set<String> configVHostMappingNames = new HashSet<>( virtualHostAsMap.keySet() );

        final Set<String> mappingsSet = new HashSet<>();

        mappingsSet.addAll( getRepoVHostMappingNames() );
        mappingsSet.addAll( getFileVHostMappingNames() );
        mappingsSet.addAll( configVHostMappingNames );

        mappingsSet.forEach( vHostMapping -> {
            final RewriteContextKey contextKey = RewriteContextKey.from( vHostMapping );

            final RewriteMappingProvider provider = fetchContextProvider( contextKey );
            contextProviders.put( contextKey, Optional.ofNullable( provider ) );

            container.put( contextKey, VirtualHostWrapper.create().
                contextKey( contextKey ).
                virtualHost( virtualHostAsMap.get( vHostMapping ) ).
                mappingProvider( fetchContextProvider( contextKey ) ).
                active( virtualHostService.isEnabled() && configVHostMappingNames.contains( vHostMapping ) ).
                build() );
        } );
    }

    private RewriteMappingProvider fetchContextProvider( final RewriteContextKey contextKey )
    {
        for ( final RewriteMappingProvider provider : providers )
        {
            final boolean providesForContext = provider.providesForContext( contextKey );
            LOG.debug( "Check if provider [{}] provides for contextKey [{}] = [{}]", provider.name(), contextKey, providesForContext );
            if ( providesForContext )
            {
                LOG.debug( "Found rewriteMapping provider [{}] for contextKey [{}]", provider.name(), contextKey );
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

    private List<String> getRepoVHostMappingNames()
    {
        return RewriteMappingRepoInitializer.createAdminContext().
            callWith( () -> {
                final FindNodesByQueryResult queryResult = nodeService.findByQuery( NodeQuery.create().
                    parent( NodePath.create( RewriteRepoMappingProvider.MAPPING_ROOT_NODE ).build() ).
                    build() );

                return queryResult.getNodeHits().getNodeIds().stream().
                    map( nodeId -> nodeService.getById( nodeId ) ).
                    map( node -> node.name().toString() ).collect( Collectors.toList() );
            } );
    }

    private List<String> getFileVHostMappingNames()
    {
        try
        {
            final List<String> result = new ArrayList<>();

            final String pattern = config.ruleFileNameTemplate().replace( "{{vhost}}", "(\\w+)" );

            Files.walk( Paths.get( HomeDir.get().toFile().getPath(), "config" ) ).
                forEach( configFile -> {
                    final String mappingName = FileNameMatcher.getMatch( configFile, pattern, 1 );

                    if ( mappingName != null )
                    {
                        result.add( mappingName );
                    }
                } );

            return result;
        }
        catch ( IOException e )
        {
            throw new UncheckedIOException( e );
        }
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
