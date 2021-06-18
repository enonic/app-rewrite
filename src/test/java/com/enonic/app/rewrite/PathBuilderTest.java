package com.enonic.app.rewrite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathBuilderTest
{
    @Test
    public void test()
    {
        assertEquals( "context/target", UrlHelper.createUrl( "context", "target" ) );
        assertEquals( "context/target", UrlHelper.createUrl( "context", "/target" ) );
    }
}
