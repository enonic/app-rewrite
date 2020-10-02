package com.enonic.app.rewrite;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;

public final class URLPathEncoder
{
    public static String encode( final String source, final Charset charset )
    {
        if ( source == null || "".equals( source ) )
        {
            return source;
        }

        final byte[] bytes = source.getBytes( charset );

        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream( bytes.length ))
        {
            for ( byte b : bytes )
            {
                if ( '/' == b )
                {
                    outputStream.write( b );
                }
                else
                {
                    outputStream.write( '%' );
                    char hex1 = Character.toUpperCase( Character.forDigit( ( b >> 4 ) & 0xF, 16 ) );
                    char hex2 = Character.toUpperCase( Character.forDigit( b & 0xF, 16 ) );
                    outputStream.write( hex1 );
                    outputStream.write( hex2 );
                }
            }

            return outputStream.toString( charset );
        }
        catch ( IOException e )
        {
            throw new UncheckedIOException( e );
        }
    }
}
