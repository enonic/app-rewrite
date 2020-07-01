package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.io.ImportResult;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class ImportResultMapper
    implements MapSerializable
{
    private final ImportResult importResult;

    public ImportResultMapper( final ImportResult importResult )
    {
        this.importResult = importResult;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.map( "importResult" );
        gen.value( "new", this.importResult.getNewRules() );
        gen.value( "failed", this.importResult.getFailed() );
        gen.value( "total", this.importResult.getTotal() );
        gen.value( "deleted", this.importResult.getDeleted() );
        gen.value( "updated", this.importResult.getUpdated() );
        gen.end();
    }
}
