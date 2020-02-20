package com.enonic.app.rewrite.provider;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.domain.RewriteVirtualHostContext;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;
import com.enonic.app.rewrite.provider.format.RewriteFormatReader;
import com.enonic.app.rewrite.vhost.VirtualHostResolver;
import com.enonic.xp.web.vhost.VirtualHost;


class RewriteRulesLocalFileProvider
    implements RewriteRulesProvider
{
    private final Logger LOG = LoggerFactory.getLogger( RewriteRulesLocalFileProvider.class );

    private RewriteEngineConfig config;

    private final VirtualHostResolver virtualHostResolver;

    RewriteRulesLocalFileProvider( final Path base, final String ruleFilePattern, final VirtualHostResolver virtualHostResolver )
    {
        this.virtualHostResolver = virtualHostResolver;

        final RewriteEngineConfig.Builder builder = RewriteEngineConfig.create();

        try
        {
            final List<VHostAndPath> items = getHostsAndPaths( base, ruleFilePattern );

            for ( final VHostAndPath item : items )
            {
                handleRewriteItem( builder, item );
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot configure rewrite-filter" );
        }

        this.config = builder.build();
    }

    @Override
    public RewriteEngineConfig provide()
    {
        return this.config;
    }

    private void handleRewriteItem( final RewriteEngineConfig.Builder builder, final VHostAndPath item )
    {
        final VirtualHost resolvedVirtualHost = this.virtualHostResolver.resolve( item.vHostName );

        if ( resolvedVirtualHost == null )
        {
            LOG.warn( "Cannot resolve vhost [%s] in vhost-config, skipping..", item.vHostName );
            return;
        }

        final RewriteVirtualHostContext context = new RewriteVirtualHostContext( resolvedVirtualHost );
        final RewriteRules.Builder rewritesBuilder = RewriteRules.create();

        LOG.info( "Fetching rewrite-config from file: [{}]", item.path );

        try (final Stream<String> stream = Files.lines( Paths.get( item.path.toString() ) ))
        {
            stream.forEach( line -> {
                final RewriteRule rule = RewriteFormatReader.read( line );
                if ( rule != null )
                {
                    rewritesBuilder.addRule( rule );
                }
            } );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot read rewrite-config from file [" + item.path + "]", e );
        }

        builder.add( context, rewritesBuilder.build() );
    }

    private List<Path> findFiles( final Path base, final String ruleFilePattern )
        throws IOException
    {
        return Files.walk( base ).filter( f -> FileNameMatcher.matches( f, ruleFilePattern ) ).collect( Collectors.toList() );
    }


    private List<VHostAndPath> getHostsAndPaths( final Path base, final String ruleFilePattern )
        throws IOException
    {
        List<VHostAndPath> hostsAndPaths = Lists.newArrayList();

        final String regExpPattern = ruleFilePattern.replace( "{{vhost}}", "([\\w|-]+)" );

        final List<Path> fileNames = findFiles( base, regExpPattern );

        fileNames.forEach( file -> {
            final String vhostName = FileNameMatcher.getMatch( file, regExpPattern, 1 );
            hostsAndPaths.add( new VHostAndPath( file, vhostName ) );
        } );

        return hostsAndPaths;
    }


    private final static class VHostAndPath
    {
        private final Path path;

        private final String vHostName;

        public VHostAndPath( final Path path, final String vHostName )
        {
            this.path = path;
            this.vHostName = vHostName;
        }
    }

}
