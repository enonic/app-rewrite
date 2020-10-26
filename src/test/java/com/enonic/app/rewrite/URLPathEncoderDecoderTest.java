package com.enonic.app.rewrite;

import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class URLPathEncoderDecoderTest
{

    @Test
    public void test()
    {
        String original = "/rénmågjøreførlan";

        String encoded = URLPathEncoder.encode( original, StandardCharsets.UTF_8 );

        Assert.assertTrue( encoded.startsWith( "/" ) );
        Assert.assertEquals( "/%72%C3%A9%6E%6D%C3%A5%67%6A%C3%B8%72%65%66%C3%B8%72%6C%61%6E", encoded );

        Assert.assertEquals( original, URLPathDecoder.decode( encoded, StandardCharsets.UTF_8 ) );
    }

}
