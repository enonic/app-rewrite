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
import com.enonic.app.rewrite.domain.SimpleRewriteContext;
import com.enonic.app.rewrite.engine.RewriteEngineConfig;


class RewriteRulesLocalFileProvider
    implements RewriteRulesProvider
{

    private final Logger LOG = LoggerFactory.getLogger( RewriteRulesLocalFileProvider.class );

    RewriteRulesLocalFileProvider( final Path base, final String ruleFilePattern )
    {
        final RewriteEngineConfig.Builder builder = RewriteEngineConfig.create();

        try
        {
            final List<VHostAndPath> items = getHostsAndPaths( base, ruleFilePattern );

            for ( final VHostAndPath item : items )
            {

                final SimpleRewriteContext context = new SimpleRewriteContext( item.vHostName );
                final RewriteRules.Builder rewritesBuilder = RewriteRules.create();

                LOG.info( "Fetching rewrite-config from file: [{}]", ruleFilePattern );

                try (Stream<String> stream = Files.lines( Paths.get( item.path.toString() ) ))
                {
                    stream.forEach( line -> {
                        final RewriteRule rule = ApacheRewriteFormatReader.read( line );
                        if ( rule != null )
                        {
                            rewritesBuilder.addRule( rule );
                        }

                    } );
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }

                builder.add( context, rewritesBuilder.build() );
            }
        }
        catch ( IOException e )
        {
            throw new RuntimeException( "Cannot configure rewrite-filter" );
        }

        System.out.println( builder.build() );

    }

    private List<Path> findFiles( final Path base, final String ruleFilePattern )
        throws IOException
    {
        Files.walk( base ).forEach( f -> System.out.println( f.toAbsolutePath() ) );

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
