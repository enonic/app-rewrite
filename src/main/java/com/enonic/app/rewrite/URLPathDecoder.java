package com.enonic.app.rewrite;

import java.nio.charset.Charset;
import java.util.Objects;

public class URLPathDecoder
{

    public static String decode( final String source, final Charset charset )
    {
        Objects.requireNonNull( charset, "Charset" );

        boolean needToChange = false;

        int numChars = source.length();

        StringBuilder builder = new StringBuilder( numChars > 500 ? numChars / 2 : numChars );

        int i = 0;

        byte[] bytes = null;

        while ( i < numChars )
        {
            char c = source.charAt( i );
            switch ( c )
            {
                case '%':
                    try
                    {
                        if ( bytes == null )
                        {
                            bytes = new byte[( numChars - i ) / 3];
                        }

                        int pos = 0;

                        while ( i + 2 < numChars && c == '%' )
                        {
                            int v = Integer.parseInt( source, i + 1, i + 3, 16 );
                            if ( v < 0 )
                            {
                                throw new IllegalArgumentException(
                                    "URLPathDecoder: Illegal hex characters in escape (%) pattern - negative value" );
                            }

                            bytes[pos++] = (byte) v;
                            i += 3;
                            if ( i < numChars )
                            {
                                c = source.charAt( i );
                            }
                        }

                        if ( i < numChars && c == '%' )
                        {
                            throw new IllegalArgumentException( "URLPathDecoder: Incomplete trailing escape (%) pattern" );
                        }

                        builder.append( new String( bytes, 0, pos, charset ) );
                    }
                    catch ( NumberFormatException var10 )
                    {
                        throw new IllegalArgumentException(
                            "URLPathDecoder: Illegal hex characters in escape (%) pattern - " + var10.getMessage() );
                    }

                    needToChange = true;
                    break;
                case '+':
                    builder.append( ' ' );
                    ++i;
                    needToChange = true;
                    break;
                case '/':
                    builder.append( '/' );
                    ++i;
                    needToChange = true;
                    break;
                default:
                    builder.append( c );
                    ++i;
            }
        }

        return needToChange ? builder.toString() : source;
    }

}
