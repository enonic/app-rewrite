package com.enonic.app.rewrite.mapping;

import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class ErrorMapper
    implements MapSerializable
{

    private final Exception exception;

    public ErrorMapper( final Exception exception )
    {
        this.exception = exception;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.map( "error" );
        gen.value( "message", exception.getMessage() );
        gen.end();
    }
}
