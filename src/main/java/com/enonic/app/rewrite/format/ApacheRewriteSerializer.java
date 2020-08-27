package com.enonic.app.rewrite.format;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;

public class ApacheRewriteSerializer
{
    private final static Logger LOG = LoggerFactory.getLogger( ApacheRewriteSerializer.class );

    private final static Pattern ruleFormat = Pattern.compile( "^\\s*RewriteRule\\s+(\\S+)\\s+(\\S+)(\\s+\\[(.*)])?" );

    private final static Pattern conditionFormat = Pattern.compile( "^\\s*RewriteCond\\s+(.*)" );

    private static final String REDIRECT_CODE_KEY = "R";

    public static final RedirectType DEFAULT_REDIRECT_TYPE = RedirectType.FOUND;

    static String serialize( final RewriteRules rewriteRules )
    {
        final StringBuilder builder = new StringBuilder();

        rewriteRules.forEach( rule -> {
            builder.append( serialize( rule ) );
            builder.append( System.lineSeparator() );
        } );

        return builder.toString();
    }

    private static String serialize( final RewriteRule rule )
    {
        final StringBuilder builder = new StringBuilder();
        builder.append( "RewriteRule" );
        builder.append( "\t" );
        builder.append( "\"" + rule.getFrom() + "\"" );
        builder.append( "\t" );
        builder.append( "\"" + rule.getTarget().path() + "\"" );
        builder.append( "\t" );
        builder.append( "[R=" + rule.getType().getHttpCode() + "]" );

        return builder.toString();
    }

    static SourceReadResult read( final BufferedReader reader )
    {
        LOG.info( "Loading apache rewriteConfigurations from file" );

        try
        {
            return doReadLines( reader );
        }
        catch ( IOException e )
        {
            LOG.error( "Could not read source", e );
            // TODO: Mark read as failed
            return SourceReadResult.create().
                addFailed().
                build();
        }
    }

    private static SourceReadResult doReadLines( final BufferedReader reader )
        throws IOException
    {
        final SourceReadResult.Builder readResult = SourceReadResult.create();

        final RewriteRules.Builder rewritesBuilder = RewriteRules.create();

        boolean inCondition = false;
        String line;

        while ( ( line = reader.readLine() ) != null )
        {
            try
            {
                final ParseLineResult result = read( line );

                if ( !inCondition && result.rule != null )
                {
                    rewritesBuilder.addRule( result.rule );
                    readResult.addNew();
                }
                else if ( inCondition && result.rule != null )
                {
                    LOG.info( "Skipping rule in condition: {}, not supported", line );
                    readResult.addUnsupported();
                    inCondition = false;
                }
                else if ( result.isCondition )
                {
                    LOG.info( "Skipping condition: {}, not supported", line );
                    inCondition = true;
                }
            }
            catch ( Exception e )
            {
                readResult.addFailed();
                LOG.error( "Cannot parse line: " + line );
            }
        }

        readResult.rules( rewritesBuilder.build() );
        return readResult.build();
    }

    private static ParseLineResult read( final String value )
    {
        if ( Strings.isNullOrEmpty( value ) )
        {
            return new ParseLineResult( false, null );
        }

        final Matcher ruleMatcher = ruleFormat.matcher( value );

        if ( ruleMatcher.matches() )
        {
            final String from = clean( ruleMatcher.group( 1 ) );
            final String target = clean( ruleMatcher.group( 2 ) );
            final Map<String, String> flags = parseFlags( ruleMatcher.group( 4 ) );

            final RedirectType type;

            if ( flags.get( REDIRECT_CODE_KEY ) != null )
            {
                Integer httpCode;
                try
                {
                    httpCode = Integer.valueOf( flags.get( REDIRECT_CODE_KEY ) );
                }
                catch ( NumberFormatException e )
                {
                    httpCode = 302;
                }

                if ( RedirectType.valueOf( httpCode ) != null )
                {
                    type = RedirectType.valueOf( httpCode );
                }
                else
                {
                    LOG.warn( "Cannot read redirect-type for code {}, using default {}", flags.get( REDIRECT_CODE_KEY ),
                              DEFAULT_REDIRECT_TYPE );
                    type = DEFAULT_REDIRECT_TYPE;
                }
            }
            else
            {
                type = DEFAULT_REDIRECT_TYPE;
            }

            return new ParseLineResult( false, RewriteRule.create().
                from( from ).
                target( clean( target ) ).
                type( type ).
                build() );
        }

        final Matcher conditionMatcher = conditionFormat.matcher( value );

        if ( conditionMatcher.matches() )
        {
            return new ParseLineResult( true, null );
        }

        return new ParseLineResult( false, null );
    }


    private static Map<String, String> parseFlags( final String value )
    {
        Map<String, String> flags = Maps.newHashMap();

        if ( Strings.isNullOrEmpty( value ) )
        {
            return flags;
        }

        final List<String> flagKeys = Arrays.asList( value.split( "," ) );

        flagKeys.forEach( f -> {
            final String[] keyValuePair = f.split( "=" );
            if ( keyValuePair.length == 1 )
            {
                flags.put( f, "true" );
            }
            else
            {
                flags.put( keyValuePair[0], keyValuePair[1] );
            }
        } );

        return flags;
    }

    private static String clean( final String original )
    {
        return cleanQuotes( original );
    }

    private static String cleanQuotes( final String original )
    {
        return original.replaceAll( "^\"|\"$", "" );
    }


    private static class ParseLineResult
    {
        private final boolean isCondition;

        private final RewriteRule rule;

        public ParseLineResult( final boolean isCondition, final RewriteRule rule )
        {
            this.isCondition = isCondition;
            this.rule = rule;
        }
    }

}
