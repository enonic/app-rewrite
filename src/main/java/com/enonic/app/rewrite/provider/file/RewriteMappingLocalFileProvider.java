package com.enonic.app.rewrite.provider.file;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import com.enonic.app.rewrite.CreateRuleParams;
import com.enonic.app.rewrite.DeleteRuleParams;
import com.enonic.app.rewrite.format.RewriteFormatReader;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;


public class RewriteMappingLocalFileProvider
    implements RewriteMappingProvider
{
    private final Logger LOG = LoggerFactory.getLogger( RewriteMappingLocalFileProvider.class );

    private final Path base;

    private final String ruleFilePattern;

    private RewriteMappings rewriteMappings;

    private RewriteMappingLocalFileProvider( final Builder builder )
    {
        base = builder.base;
        ruleFilePattern = builder.ruleFilePattern;
    }

    @Override
    public boolean readOnly()
    {
        return true;
    }

    @Override
    public String name()
    {
        return "LocalFile";
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey contextKey )
    {
        if ( this.rewriteMappings == null )
        {
            this.rewriteMappings = doGetAllRewriteMappings();
        }

        return rewriteMappings.getRewriteMapping( contextKey );
    }

    private RewriteMappings doGetAllRewriteMappings()
    {
        final RewriteMappings.Builder builder = RewriteMappings.create();

        try
        {
            final List<VHostAndPath> items = getHostsAndPaths( this.base, this.ruleFilePattern );

            for ( final VHostAndPath item : items )
            {
                handleRewriteItem( builder, item );
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot configure rewrite-filter" );
        }

        return builder.build();
    }

    @Override
    public void store( final RewriteMapping rewriteMapping )
    {
        throw new RuntimeException( "Cannot store to provider " + this.getClass().getName() );
    }

    @Override
    public void create( final RewriteContextKey rewriteContextKey )
    {
        throw new RuntimeException( "Cannot create context " + this.getClass().getName() );
    }

    @Override
    public void delete( final RewriteContextKey rewriteContextKey )
    {
        throw new RuntimeException( "Cannot delete from provider " + this.getClass().getName() );
    }

    @Override
    public void createRule( final CreateRuleParams params )
    {
        throw new RuntimeException( "Cannot create rule in provider " + this.getClass().getName() );
    }

    @Override
    public void deleteRule( final DeleteRuleParams params )
    {
        throw new RuntimeException( "Cannot delete rule in provider " + this.getClass().getName() );
    }

    private void handleRewriteItem( final RewriteMappings.Builder builder, final VHostAndPath item )
    {
        final RewriteContextKey contextKey = new RewriteContextKey( item.vHostName );
        final RewriteRules.Builder rewritesBuilder = RewriteRules.create();

        LOG.info( "Fetching rewrite-config from file: [{}]", item.path );

        final AtomicInteger i = new AtomicInteger( 0 );
        try (final Stream<String> stream = Files.lines( Paths.get( item.path.toString() ) ))
        {
            stream.forEach( line -> {
                final RewriteRule rule = RewriteFormatReader.read( line, i.getAndIncrement() );
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

        builder.add( RewriteMapping.create().
            contextKey( contextKey ).
            rewriteRules( rewritesBuilder.build() ).
            build() );
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

    public static Builder create()
    {
        return new Builder();
    }

    private final static class VHostAndPath
    {
        private final Path path;

        private final String vHostName;

        VHostAndPath( final Path path, final String vHostName )
        {
            this.path = path;
            this.vHostName = vHostName;
        }
    }

    public static final class Builder
    {
        private Path base;

        private String ruleFilePattern;

        private Builder()
        {
        }

        public Builder base( final Path base )
        {
            this.base = base;
            return this;
        }

        public Builder ruleFilePattern( final String ruleFilePattern )
        {
            this.ruleFilePattern = ruleFilePattern;
            return this;
        }

        public RewriteMappingLocalFileProvider build()
        {
            return new RewriteMappingLocalFileProvider( this );
        }
    }
}
