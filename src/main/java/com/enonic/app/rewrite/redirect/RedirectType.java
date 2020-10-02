package com.enonic.app.rewrite.redirect;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public enum RedirectType
{
    MOVED_PERMANENTLY( 301 ), FOUND( 302 ), TEMPORARILY_REDIRECT( 307 ), PERMANENT_REDIRECT( 308 );

    private int httpCode;

    private final static Map<Integer, RedirectType> MAP =
        stream( RedirectType.values() ).collect( toMap( type -> type.httpCode, leg -> leg ) );

    RedirectType( final int httpCode )
    {
        this.httpCode = httpCode;
    }


    public static RedirectType valueOf( int httpCode )
    {
        return MAP.get( httpCode );
    }

    public int getHttpCode()
    {
        return httpCode;
    }

}
