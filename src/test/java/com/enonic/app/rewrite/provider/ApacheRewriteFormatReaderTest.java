package com.enonic.app.rewrite.provider;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RewriteRule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApacheRewriteFormatReaderTest
{


    @Test
    void testRule()
    {
        final RewriteRule rule =
            ApacheRewriteFormatReader.read( "RewriteRule    \"^/bedrift/pensjon/(.*)$\" \"/virksomhet/pensjon/$1\" [R=301,NC,L]" );

        assertNotNull( rule );
        assertEquals( "^/bedrift/pensjon/(.*)$", rule.getFrom() );
        assertEquals( "/virksomhet/pensjon/$1", rule.getTarget().toString() );
    }
}