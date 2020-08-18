package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.ie.ImportResult;
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
        gen.value( "failed", this.importResult.isFailed() );
        gen.value( "new", this.importResult.getNewRules() );
        gen.value( "errors", this.importResult.getErrors() );
        gen.value( "total", this.importResult.getTotal() );
        gen.value( "deleted", this.importResult.getDeleted() );
        gen.value( "updated", this.importResult.getUpdated() );
        gen.value( "unsupported", this.importResult.getUnsupported() );
        gen.end();
    }
}
