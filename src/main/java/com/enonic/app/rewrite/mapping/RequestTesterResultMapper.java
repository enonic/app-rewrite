package com.enonic.app.rewrite.mapping;

import org.eclipse.jetty.util.URIUtil;

import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectTarget;
import com.enonic.app.rewrite.requesttester.IncomingRequest;
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
        result.getMatchList().forEach( redirectMatch -> serialize( gen, redirectMatch ) );
        gen.end();
    }


    private void serialize( final MapGenerator gen, final RedirectTestResult testResult )
    {
        gen.map();
        serialize( gen, testResult.getIncomingRequest() );
        serialize( gen, testResult.redirectMatch() );
        gen.end();
    }

    private void serialize( final MapGenerator gen, final IncomingRequest incomingRequest )
    {
        gen.map( "incomingRequest" );

        if ( incomingRequest != null )
        {
            gen.value( "url", URIUtil.decodePath( incomingRequest.getRequestUrl() ) );

            final VirtualHost matchingVHost = incomingRequest.getMatchingVHost();

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

    private void serialize( final MapGenerator gen, final RedirectMatch match )
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
        gen.value( "url", URIUtil.decodePath( target.getTargetPath() ) );
        gen.value( "type", target instanceof RedirectExternal ? "absolute" : "relative" );
        gen.end();
    }
}
