package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.domain.Redirect;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;
import com.enonic.xp.web.vhost.VirtualHost;

public class RequestTesterResultMapper
    implements MapSerializable
{
    private final RequestTesterResult result;

    public RequestTesterResultMapper( final RequestTesterResult result )
    {
        this.result = result;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        gen.map( "result" );
        serialize( gen, this.result.getVirtualHost() );
        serialize( gen, this.result.getRedirect() );
        gen.end();
    }

    private void serialize( final MapGenerator gen, final VirtualHost virtualHost )
    {
        gen.map( "virtualHost" );

        if ( virtualHost != null )
        {
            gen.value( "name", virtualHost.getName() );
            gen.value( "source", virtualHost.getSource() );
            gen.value( "target", virtualHost.getTarget() );
            gen.value( "host", virtualHost.getHost() );
            gen.value( "defaultIdProvider", virtualHost.getDefaultIdProviderKey() );
        }

        gen.end();
    }

    private void serialize( final MapGenerator gen, final Redirect redirect )
    {
        gen.map( "rewrite" );
        if ( redirect != null )
        {
            gen.value( "target", redirect.getRedirectTarget() );
            gen.value( "type", redirect.getType() );
        }
        gen.end();
    }
}
