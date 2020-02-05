package com.enonic.app.rewrite.context;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SiteContextResolverTest
{

    @Test
    void test1()
    {
        final String resolve = SiteContextResolver.resolve( "/site/fisk/ost/løk" );
        assertEquals( "/site/fisk/ost/", resolve );
    }

    @Test
    void test2()
    {
        final String resolve = SiteContextResolver.resolve( "/site/fisk/ost/løk/spenol" );
        assertEquals( "/site/fisk/ost/", resolve );
    }
}
