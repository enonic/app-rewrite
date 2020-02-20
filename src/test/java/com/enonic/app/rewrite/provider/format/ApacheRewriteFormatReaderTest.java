package com.enonic.app.rewrite.provider.format;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.provider.format.ApacheRewriteFormatReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApacheRewriteFormatReaderTest
{


    @Test
    void testRule()
    {
        final RewriteRule rule =
            ApacheRewriteFormatReader.read( "RewriteRule    \"^/bedrift/pensjon/(.*)$\" \"/virksomhet/pensjon/$1\" [R=307,NC,L]" );

        assertNotNull( rule );
        assertEquals( "^/bedrift/pensjon/(.*)$", rule.getFrom() );
        assertEquals( "/virksomhet/pensjon/$1", rule.getTarget().toString() );
        assertEquals( 307, rule.getType().getHttpCode() );
    }

    @Test
    void testRule2()
    {
        final RewriteRule rule = ApacheRewriteFormatReader.read( "RewriteRule    \"^/bedrift/pensjon/(.*)$\" \"/virksomhet/pensjon/$1\"" );

        assertNotNull( rule );
        assertEquals( "^/bedrift/pensjon/(.*)$", rule.getFrom() );
        assertEquals( "/virksomhet/pensjon/$1", rule.getTarget().toString() );
        assertEquals( 301, rule.getType().getHttpCode() );
    }
}