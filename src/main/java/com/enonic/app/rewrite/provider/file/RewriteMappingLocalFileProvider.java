package com.enonic.app.rewrite.provider.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;

import com.enonic.app.rewrite.CreateRuleParams;
import com.enonic.app.rewrite.DeleteRuleParams;
import com.enonic.app.rewrite.format.SourceFormatResolver;
import com.enonic.app.rewrite.format.SourceReadResult;
import com.enonic.app.rewrite.format.SourceReader;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;


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
        return "File";
    }

    @Override
    public boolean providesForContext( final RewriteContextKey contextKey )
    {
        final String pattern = createContextPattern( ruleFilePattern, contextKey.toString() );

        try
        {
            final List<Path> files = findFiles( this.base, pattern );
            return files.size() > 0;
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot search for files", e );
        }
    }

    private String createContextPattern( final String ruleFilePattern, final String s )
    {
        return ruleFilePattern.replace( "{{vhost}}", s );
    }

    @Override
    public RewriteMapping getRewriteMapping( final RewriteContextKey contextKey )
    {
        final String pattern = createContextPattern( ruleFilePattern, contextKey.toString() );

        try
        {
            final List<Path> files = findFiles( this.base, pattern );
            if ( files.size() > 0 )
            {
                final Path path = files.get( 0 );
                LOG.info( "Loading rewrite-mapping for contextKey [{}] from file: [{}]", contextKey, path );

                try (final BufferedReader reader = Files.newBufferedReader( Paths.get( path.toString() ), Charsets.UTF_8 ))
                {
                    final SourceReadResult rewriteRules =
                        SourceReader.read( reader, SourceFormatResolver.resolve( path.toFile().getName() ) );
                    return RewriteMapping.create().
                        contextKey( contextKey ).
                        rewriteRules( rewriteRules.getRules() ).
                        build();
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( "Cannot read rewrite-config from file [" + path + "]", e );
                }
            }
            else
            {
                return null;
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot search for files", e );
        }
    }

    private RewriteMappings doGetAllRewriteMappings()
    {
        final RewriteMappings.Builder builder = RewriteMappings.create();

        final List<VHostAndPath> items = getHostsAndPaths( this.base, this.ruleFilePattern );

        for ( final VHostAndPath item : items )
        {
            handleRewriteItem( builder, item );
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

        LOG.info( "handleRewriteItem for item: " + item );

        try (final BufferedReader reader = Files.newBufferedReader( Paths.get( item.path.toString() ), Charsets.UTF_8 ))
        {
            final SourceReadResult rewriteRules = SourceReader.read( reader, SourceFormatResolver.resolve( item.path.toFile().getName() ) );
            builder.add( RewriteMapping.create().
                contextKey( contextKey ).
                rewriteRules( rewriteRules.getRules() ).
                build() );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot read rewrite-config from file [" + item.path + "]", e );
        }
    }

    private List<Path> findFiles( final Path base, final String ruleFilePattern )
        throws IOException
    {
        return Files.walk( base ).filter( f -> FileNameMatcher.matches( f, ruleFilePattern ) ).collect( Collectors.toList() );
    }


    private List<VHostAndPath> getHostsAndPaths( final Path base, final String ruleFilePattern )
    {
        final List<VHostAndPath> hostsAndPaths = Lists.newArrayList();

        final String regExpPattern = createContextPattern( ruleFilePattern, "([\\w|-]+)" );

        final List<Path> fileNames;
        try
        {
            fileNames = findFiles( base, regExpPattern );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot configure rewrite-filter", e );
        }

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
