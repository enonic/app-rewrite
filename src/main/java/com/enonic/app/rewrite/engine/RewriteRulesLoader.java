package com.enonic.app.rewrite.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jetty.util.URIUtil;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteMappings;
import com.enonic.app.rewrite.domain.RewriteRule;

class RewriteRulesLoader
{

    private static final Pattern URL_PATTERN = Pattern.compile( "^(https?)://(?:www\\.)?([\\w-_.]+):?([0-9]{2,4})?(/[\\w\\W]*)" );

    private static final int HOST_GROUP_INDEX = 2;

    private static final int PATH_GROUP_INDEX = 4;

    static Map<RewriteContextKey, RulePatterns> load( final RewriteMappings mappings )
    {
        final Map<RewriteContextKey, RulePatterns> rewriteMap = new HashMap<>();

        for ( final RewriteMapping rewriteMapping : mappings )
        {
            final RulePatterns.Builder patternsBuilder = RulePatterns.create();

            for ( final RewriteRule rule : rewriteMapping )
            {
                final String path = normalizePath( rule.getTarget().path(), rewriteMapping.getContextKey() );

                final RulePattern pattern = RulePattern.create().
                    target( URIUtil.encodePath( path ) ).
                    type( rule.getType() ).
                    pattern( rule.getFrom() ).
                    build();
                patternsBuilder.rule( pattern );
            }

            rewriteMap.put( rewriteMapping.getContextKey(), patternsBuilder.build() );
        }

        return rewriteMap;
    }

    private static String normalizePath( String path, final RewriteContextKey contextKey )
    {
        final Matcher matcher = URL_PATTERN.matcher( path );

        if ( matcher.matches() )
        {
            final String hostname = matcher.group( HOST_GROUP_INDEX );

            if ( hostname != null && hostname.startsWith( contextKey.toString() ) )
            {
                path = matcher.group( PATH_GROUP_INDEX );
            }
        }

        return path;
    }

}
