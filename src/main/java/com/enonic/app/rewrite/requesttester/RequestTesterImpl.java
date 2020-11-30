package com.enonic.app.rewrite.requesttester;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.UpdateRuleParams;
import com.enonic.app.rewrite.engine.ExtRulePattern;
import com.enonic.app.rewrite.engine.RulePattern;
import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectTarget;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;
import com.enonic.xp.web.vhost.VirtualHostResolver;

@Component(enabled = false)
public class RequestTesterImpl
    implements RequestTester
{

    private static final Integer THRESHOLD = 1_000;

    private VirtualHostResolver virtualHostResolver;

    private RewriteService rewriteService;

    @Override
    public RequestTesterResult testRequest( final String requestURL )
    {
        return doExecute( requestURL, null );
    }

    @Override
    public boolean hasLoops( final UpdateRuleParams params )
    {
        final ExtRulePattern extRulePattern = new ExtRulePattern( params.getPosition(), RulePattern.create().
            type( RedirectType.TEMPORARILY_REDIRECT ).
            pattern( params.getSource() ).
            target( params.getTarget() ).
            build() );

        final String requestURL = Paths.get( params.getContextKey().toString(), params.getTarget() ).toString();

        final RequestTesterResult redirectTestResult = doExecute( requestURL, extRulePattern );

        return redirectTestResult.getResultState() == RequestTesterResult.TestResultState.LOOP;
    }

    private RequestTesterResult doExecute( final String requestURL, final ExtRulePattern extRulePattern )
    {
        final RequestTesterResult.Builder builder = RequestTesterResult.create();

        if ( requestURL == null || requestURL.isEmpty() )
        {
            return builder.build();
        }

        RedirectTestResult redirectTestResult = doTest( requestURL, extRulePattern );
        builder.add( redirectTestResult );

        final List<Redirect> redirectedTargets = new ArrayList<>();

        int counter = 0;

        while ( redirectTestResult.redirectMatch() != null )
        {
            counter++;

            if ( counter == THRESHOLD )
            {
                return builder.error().build();
            }

            final Redirect redirect = redirectTestResult.redirectMatch().getRedirect();

            if ( containsInRedirectedTargets( redirectedTargets, redirect ) )
            {
                return builder.loop().build();
            }

            redirectedTargets.add( redirect );
            String newTargetPath = getNextTargetPath( redirectTestResult.getMatchingVHost(), redirect );
            redirectTestResult = doTest( newTargetPath, extRulePattern );
            builder.add( redirectTestResult );
        }

        return builder.build();
    }

    private boolean containsInRedirectedTargets( final List<Redirect> redirectedTargets, final Redirect redirect )
    {
        return redirectedTargets.stream().anyMatch(
            r -> r.getRedirectTarget().getTargetPath().equals( redirect.getRedirectTarget().getTargetPath() ) );
    }

    private String getNextTargetPath( final VirtualHost virtualHost, final Redirect redirect )
    {
        final RedirectTarget newTarget = redirect.getRedirectTarget();

        if ( newTarget instanceof RedirectExternal )
        {
            return newTarget.getTargetPath();
        }
        else
        {
            return Paths.get( virtualHost.getHost(), newTarget.getTargetPath() ).toString();
        }
    }

    private RedirectTestResult doTest( final String requestURL, final ExtRulePattern extRulePattern )
    {
        final RewriteTesterRequest req = new RewriteTesterRequest( requestURL );

        final VirtualHost virtualHost = getAndSetVHost( req );

        RedirectMatch redirectMatch = null;

        if ( virtualHost != null )
        {
            redirectMatch = this.rewriteService.process( req, extRulePattern );
        }

        return new RedirectTestResult( requestURL, virtualHost, redirectMatch );
    }

    private VirtualHost getAndSetVHost( final RewriteTesterRequest req )
    {
        final VirtualHost virtualHost = virtualHostResolver.resolveVirtualHost( req );

        if ( virtualHost != null )
        {
            VirtualHostHelper.setVirtualHost( req, virtualHost );
        }

        return virtualHost;
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
