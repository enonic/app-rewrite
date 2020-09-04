package com.enonic.app.rewrite.format;

import java.io.BufferedReader;

import org.junit.jupiter.api.Test;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ApacheRewriteSerializerTest
{


    @Test
    void ruleTest()
        throws Exception
    {
        final ByteSource source =
            ByteSource.wrap( "RewriteRule    \"^/bedrift/pensjon/(.*)$\" \"/virksomhet/pensjon/$1\" [R=307,NC,L]".getBytes() );

        try (BufferedReader reader = source.asCharSource( Charsets.UTF_8 ).openBufferedStream())
        {
            final RewriteRules rules = ApacheRewriteSerializer.read( reader ).getRules();
            assertTrue( rules.iterator().hasNext() );
            final RewriteRule rule = rules.iterator().next();
            assertEquals( "^/bedrift/pensjon/(.*)$", rule.getFrom() );
            assertEquals( "/virksomhet/pensjon/$1", rule.getTarget().toString() );
            assertEquals( 307, rule.getType().getHttpCode() );
        }
    }

    @Test
    void ruleTest2()
        throws Exception
    {
        final ByteSource source = ByteSource.wrap( "RewriteRule    \"^/bedrift/pensjon/(.*)$\" \"/virksomhet/pensjon/$1\"".getBytes() );
        try (BufferedReader reader = source.asCharSource( Charsets.UTF_8 ).openBufferedStream())
        {
            final RewriteRules rules = ApacheRewriteSerializer.read( reader ).getRules();
            assertTrue( rules.iterator().hasNext() );
            final RewriteRule rule = rules.iterator().next();
            assertEquals( "^/bedrift/pensjon/(.*)$", rule.getFrom() );
            assertEquals( "/virksomhet/pensjon/$1", rule.getTarget().toString() );
            assertEquals( 301, rule.getType().getHttpCode() );
        }
    }
}