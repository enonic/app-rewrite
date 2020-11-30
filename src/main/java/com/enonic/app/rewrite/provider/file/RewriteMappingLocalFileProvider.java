package com.enonic.app.rewrite.provider.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.DeleteRuleParams;
import com.enonic.app.rewrite.UpdateRuleParams;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.format.SourceFormatResolver;
import com.enonic.app.rewrite.format.SourceReadResult;
import com.enonic.app.rewrite.format.SourceReader;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;


public class RewriteMappingLocalFileProvider
    implements RewriteMappingProvider
{
    private final Logger LOG = LoggerFactory.getLogger( RewriteMappingLocalFileProvider.class );

    private final Path base;

    private final String ruleFilePattern;

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
            throw new UncheckedIOException( "Cannot search for files", e );
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
            if ( !files.isEmpty() )
            {
                final Path path = files.get( 0 );
                LOG.info( "Loading rewrite-mapping for contextKey [{}] from file: [{}]", contextKey, path );

                try (final BufferedReader reader = Files.newBufferedReader( Paths.get( path.toString() ), StandardCharsets.UTF_8 ))
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
                    throw new UncheckedIOException( "Cannot read rewrite-config from file [" + path + "]", e );
                }
            }
            else
            {
                return null;
            }
        }
        catch ( IOException e )
        {
            throw new UncheckedIOException( "Cannot search for files", e );
        }
    }

    @Override
    public void store( final RewriteMapping rewriteMapping )
    {
        throw new UnsupportedOperationException( "Cannot store to provider " + this.getClass().getName() );
    }

    @Override
    public void create( final RewriteContextKey rewriteContextKey )
    {
        throw new UnsupportedOperationException( "Cannot create context " + this.getClass().getName() );
    }

    @Override
    public void delete( final RewriteContextKey rewriteContextKey )
    {
        throw new UnsupportedOperationException( "Cannot delete from provider " + this.getClass().getName() );
    }

    @Override
    public void saveRule( final UpdateRuleParams params )
    {
        throw new UnsupportedOperationException( "Cannot save a rule in provider " + this.getClass().getName() );
    }

    @Override
    public void deleteRule( final DeleteRuleParams params )
    {
        throw new UnsupportedOperationException( "Cannot delete rule in provider " + this.getClass().getName() );
    }

    private List<Path> findFiles( final Path base, final String ruleFilePattern )
        throws IOException
    {
        return Files.walk( base ).filter( f -> FileNameMatcher.matches( f, ruleFilePattern ) ).collect( Collectors.toList() );
    }

    public static Builder create()
    {
        return new Builder();
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

        public Builder ruleFileNameTemplate( final String ruleFilePattern )
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
