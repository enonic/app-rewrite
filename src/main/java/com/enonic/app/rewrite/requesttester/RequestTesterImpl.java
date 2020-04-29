package com.enonic.app.rewrite.requesttester;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;

@Component(immediate = true)
public class RequestTesterImpl
    implements RequestTester
{
    private VirtualHostConfig config;

    private RewriteService rewriteService;

    private static Integer THRESHOLD = 100;

    @Override
    public RequestTesterResult testRequest( final String requestURL )
    {
        List<Redirect> redirectedTargets = Lists.newArrayList();

        final RequestTesterResult.Builder builder = RequestTesterResult.create();

        if ( Strings.isNullOrEmpty( requestURL ) )
        {
            return builder.build();
        }

        RedirectTestResult redirectTestResult = doTest( requestURL );

        int counter = 0;

        while ( redirectTestResult != null )
        {
            builder.add( redirectTestResult );

            final RedirectMatch match = redirectTestResult.getMatch();

            if ( match != null )
            {
                final Redirect redirect = match.getRedirect();

                System.out.println( "Current redirect: " + redirect );

                if ( counter++ > 100 || redirectedTargets.contains( redirect ) )
                {
                    throw new RedirectLoopException( redirectedTargets, redirect );
                }

                redirectedTargets.add( redirect );

                final Path newPath =
                    Paths.get( redirectTestResult.getVirtualHost().getHost(), redirect.getRedirectTarget().getTargetPath() );
                redirectTestResult = doTest( newPath.toString() );
            }
            else
            {
                break;
            }
        }

        return builder.build();
    }

    private RedirectTestResult doTest( final String requestURL )
    {

        final RewriteTesterRequest req = new RewriteTesterRequest( requestURL );
        getAndSetVHost( req );
        final VirtualHost virtualHost = VirtualHostHelper.getVirtualHost( req );
        final RedirectMatch redirect = this.rewriteService.process( req );

        if ( virtualHost == null && redirect == null )
        {
            return null;
        }

        return new RedirectTestResult( virtualHost, redirect );
    }

    private void getAndSetVHost( final RewriteTesterRequest req )
    {
        final VirtualHostMapping virtualHostMapping = this.config.getMappings().resolve( req );
        if ( virtualHostMapping != null )
        {
            VirtualHostHelper.setVirtualHost( req, virtualHostMapping );
        }
    }

    @Reference
    public void setConfig( final VirtualHostConfig config )
    {
        this.config = config;
    }

    @Reference
    public void setRewriteService( final RewriteService rewriteService )
    {
        this.rewriteService = rewriteService;
    }
}
