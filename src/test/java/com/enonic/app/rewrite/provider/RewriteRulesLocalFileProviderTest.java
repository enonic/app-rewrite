package com.enonic.app.rewrite.provider;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.tuckey.web.filters.urlrewrite.Conf;
import org.tuckey.web.filters.urlrewrite.NormalRule;
import org.tuckey.web.filters.urlrewrite.Rule;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class RewriteRulesLocalFileProviderTest
{

    @Test
    void name()
    {

        final RewriteRulesLocalFileProvider provider = new RewriteRulesLocalFileProvider(
            RewriteRulesLocalFileProviderTest.class.getResource( "test-vhost-placeholder.xml" ).getPath() );

        final Conf conf = provider.get();

        assertNotNull( conf );

        final List<Rule> rules = (List<Rule>) conf.getRules();

        rules.forEach( r -> {

            if ( r instanceof NormalRule )
            {
                final NormalRule nr = (NormalRule) r;
                System.out.println( "From: " + nr.getFrom() + " - to " + nr.getTo() + " - type: " + nr.getToType() );
            }

        } );


    }
}