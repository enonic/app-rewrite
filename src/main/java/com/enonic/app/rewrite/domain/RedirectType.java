package com.enonic.app.rewrite.domain;

import java.util.Map;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toMap;

public enum RedirectType
{
    MOVED_PERMANENTLY( 301 ), TEMPORARILY_REDIRECT( 307 ), PERMANENT_REDIRECT( 308 );

    private int httpCode;

    private final static Map<Integer, RedirectType> map =
        stream( RedirectType.values() ).collect( toMap( type -> type.httpCode, leg -> leg ) );

    static
    {
        for ( RedirectType redirect : RedirectType.values() )
        {
            map.put( redirect.httpCode, redirect );
        }
    }

    RedirectType( final int httpCode )
    {
        this.httpCode = httpCode;
    }


    public static RedirectType valueOf( int httpCode )
    {
        return map.get( httpCode );
    }

    public int getHttpCode()
    {
        return httpCode;
    }
}
