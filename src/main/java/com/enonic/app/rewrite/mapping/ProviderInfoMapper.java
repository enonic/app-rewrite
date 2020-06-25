package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class ProviderInfoMapper
    implements MapSerializable
{
    private ProviderInfo providerInfo;

    public ProviderInfoMapper( final ProviderInfo providerInfo )
    {
        this.providerInfo = providerInfo;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.map( "providerInfo" );
        gen.value( "name", providerInfo.getName() );
        gen.value( "readOnly", providerInfo.isReadOnly() );
        gen.end();
    }
}
