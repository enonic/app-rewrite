package com.enonic.app.rewrite.requesttester;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectTarget;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;
import com.enonic.xp.web.vhost.VirtualHostResolver;

@Component(enabled = false)
public class RequestTesterImpl
    implements RequestTester
{

    private static final Integer THRESHOLD = 100;

    private VirtualHostResolver virtualHostResolver;

    private RewriteService rewriteService;

    @Override
    public RequestTesterResult testRequest( final String requestURL )
    {
        final List<Redirect> redirectedTargets = new ArrayList<>();

        final RequestTesterResult.Builder builder = RequestTesterResult.create();

        if ( requestURL == null || requestURL.isEmpty() )
        {
            return builder.build();
        }

        RedirectTestResult redirectTestResult = doTest( requestURL );
        builder.add( redirectTestResult );

        int counter = 0;

        while ( redirectTestResult.redirectMatch() != null )
        {
            final RedirectMatch match = redirectTestResult.redirectMatch();

            if ( match != null )
            {
                final Redirect redirect = match.getRedirect();

                if ( redirectedTargets.contains( redirect ) )
                {
                    return builder.loop().build();
                }
                else if ( counter > THRESHOLD )
                {
                    return builder.error().build();
                }

                redirectedTargets.add( redirect );
                String newTargetPath = getNextTargetPath( redirectTestResult, redirect );
                redirectTestResult = doTest( newTargetPath );
                builder.add( redirectTestResult );
            }
            else
            {
                break;
            }
        }

        return builder.build();
    }

    private String getNextTargetPath( final RedirectTestResult redirectTestResult, final Redirect redirect )
    {
        final RedirectTarget newTarget = redirect.getRedirectTarget();
        String newTargetPath;

        if ( newTarget instanceof RedirectExternal )
        {
            newTargetPath = newTarget.getTargetPath();
        }
        else
        {
            final IncomingRequest incomingRequest = redirectTestResult.getIncomingRequest();
            newTargetPath = Paths.get( incomingRequest.getMatchingVHost().getHost(), newTarget.getTargetPath() ).toString();
        }
        return newTargetPath;
    }

    private RedirectTestResult doTest( final String requestURL )
    {
        final RewriteTesterRequest req = new RewriteTesterRequest( requestURL );
        getAndSetVHost( req );

        final IncomingRequest incomingRequest = createIncomingRequest( requestURL, req );

        RedirectMatch redirect = null;

        if ( incomingRequest.getMatchingVHost() != null )
        {
            redirect = this.rewriteService.process( req );
        }

        return new RedirectTestResult( incomingRequest, redirect );
    }

    private IncomingRequest createIncomingRequest( final String requestURL, final RewriteTesterRequest req )
    {
        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( req );
        return new IncomingRequest( requestURL, virtualHost );
    }

    private void getAndSetVHost( final RewriteTesterRequest req )
    {
        final VirtualHost virtualHost = virtualHostResolver.resolveVirtualHost( req );

        if ( virtualHost != null )
        {
            VirtualHostHelper.setVirtualHost( req, virtualHost );
        }
    }

    @Reference
    public void setVirtualHostResolver( final VirtualHostResolver virtualHostResolver )
    {
        this.virtualHostResolver = virtualHostResolver;
    }

    @Reference
    public void setRewriteService( final RewriteService rewriteService )
    {
        this.rewriteService = rewriteService;
    }

}
