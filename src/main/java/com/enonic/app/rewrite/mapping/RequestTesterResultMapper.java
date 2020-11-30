package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectTarget;
import com.enonic.app.rewrite.requesttester.RedirectTestResult;
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

        gen.value( "state", result.getResultState().name() );
        gen.array( "steps" );
        result.getMatchList().forEach( redirectResult -> serialize( gen, redirectResult ) );
        gen.end();
    }


    private void serialize( final MapGenerator gen, final RedirectTestResult redirectResult )
    {
        gen.map();
        serializeIncomingRequest( gen, redirectResult );
        serializeRewrite( gen, redirectResult.redirectMatch() );
        gen.end();
    }

    private void serializeIncomingRequest( final MapGenerator gen, final RedirectTestResult redirectResult )
    {
        gen.map( "incomingRequest" );

        if ( redirectResult != null )
        {
            if ( redirectResult.getRequestUrl() != null )
            {
                gen.value( "url", redirectResult.getRequestUrl() );
            }

            final VirtualHost matchingVHost = redirectResult.getMatchingVHost();

            if ( matchingVHost != null )
            {
                gen.map( "matchingVHost" );
                gen.value( "name", matchingVHost.getName() );
                gen.value( "source", matchingVHost.getSource() );
                gen.value( "target", matchingVHost.getTarget() );
                gen.value( "host", matchingVHost.getHost() );
                gen.value( "defaultIdProvider", matchingVHost.getDefaultIdProviderKey() );
                gen.end();
            }
        }

        gen.end();
    }

    private void serializeRewrite( final MapGenerator gen, final RedirectMatch match )
    {
        gen.map( "rewrite" );
        if ( match != null )
        {
            gen.value( "matchId", match.getMatchId() );
            gen.value( "type", match.getRedirect().getType() );
            gen.value( "code", match.getRedirect().getType().getHttpCode() );
            serialize( gen, match.getRedirect().getRedirectTarget() );
        }
        gen.end();
    }

    private void serialize( final MapGenerator gen, final RedirectTarget target )
    {
        gen.map( "target" );
        gen.value( "url", target.getTargetPath() );
        gen.value( "type", target instanceof RedirectExternal ? "absolute" : "relative" );
        gen.end();
    }
}
