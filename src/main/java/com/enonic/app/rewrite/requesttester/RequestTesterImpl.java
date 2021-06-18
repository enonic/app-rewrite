package com.enonic.app.rewrite.requesttester;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.RequestTesterParams;
import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.UpdateRuleParams;
import com.enonic.app.rewrite.UrlHelper;
import com.enonic.app.rewrite.engine.ExtRulePattern;
import com.enonic.app.rewrite.engine.RulePattern;
import com.enonic.app.rewrite.redirect.Redirect;
import com.enonic.app.rewrite.redirect.RedirectExternal;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.redirect.RedirectTarget;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostHelper;
import com.enonic.xp.web.vhost.VirtualHostService;

@Component(enabled = false)
public class RequestTesterImpl
    implements RequestTester
{

    private static final Integer THRESHOLD = 1_000;

    private RewriteService rewriteService;

    private VirtualHostService virtualHostService;

    @Override
    public RequestTesterResult testRequest( final RequestTesterParams params )
    {
        return doExecute( params.getHost(), params.getRequestPath(), params.getRewriteContext(), null );
    }

    @Override
    public boolean hasLoops( final UpdateRuleParams params )
    {
        final ExtRulePattern extRulePattern = new ExtRulePattern( params.getPosition(), RulePattern.create().
            type( RedirectType.TEMPORARILY_REDIRECT ).
            pattern( params.getSource() ).
            target( params.getTarget() ).
            build() );

        final RequestTesterResult redirectTestResult =
            doExecute( params.getHost(), params.getTarget(), params.getContextKey().toString(), extRulePattern );

        return redirectTestResult.getResultState() == RequestTesterResult.TestResultState.LOOP;
    }

    private RequestTesterResult doExecute( final String host, final String requestPath, final String contextKey,
                                           final ExtRulePattern extRulePattern )
    {
        final RequestTesterResult.Builder builder = RequestTesterResult.create();

        final String requestURL = UrlHelper.createUrl( host, requestPath );

        RedirectTestResult redirectTestResult = doTest( requestURL, contextKey, extRulePattern );
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
            String newTargetPath = getNextTargetPath( host, redirect );
            redirectTestResult = doTest( newTargetPath, contextKey, extRulePattern );
            builder.add( redirectTestResult );
        }

        return builder.build();
    }

    private boolean containsInRedirectedTargets( final List<Redirect> redirectedTargets, final Redirect redirect )
    {
        return redirectedTargets.stream().anyMatch(
            r -> r.getRedirectTarget().getTargetPath().equals( redirect.getRedirectTarget().getTargetPath() ) );
    }

    private String getNextTargetPath( final String host, final Redirect redirect )
    {
        final RedirectTarget newTarget = redirect.getRedirectTarget();

        if ( newTarget instanceof RedirectExternal )
        {
            return newTarget.getTargetPath();
        }
        else
        {
            return UrlHelper.createUrl( host, newTarget.getTargetPath() );
        }
    }

    private RedirectTestResult doTest( final String requestURL, final String contextKey, final ExtRulePattern extRulePattern )
    {
        final RewriteTesterRequest req = new RewriteTesterRequest( requestURL );

        final VirtualHost virtualHost = getAndSetVHost( req, contextKey );

        RedirectMatch redirectMatch = null;

        if ( virtualHost != null )
        {
            redirectMatch = this.rewriteService.process( req, extRulePattern );
        }

        return new RedirectTestResult( requestURL, virtualHost, redirectMatch );
    }

    private VirtualHost getAndSetVHost( final RewriteTesterRequest req, final String contextKey )
    {
        final Optional<VirtualHost> virtualHostOpt = virtualHostService.getVirtualHosts().stream().
            filter( v -> v.getName().equalsIgnoreCase( contextKey ) ).
            findFirst();

        if ( virtualHostOpt.isPresent() )
        {
            final VirtualHost virtualHost = virtualHostOpt.get();

            VirtualHostHelper.setVirtualHost( req, virtualHost );

            return virtualHost;
        }

        return null;
    }

    @Reference
    public void setVirtualHostService( final VirtualHostService virtualHostService )
    {
        this.virtualHostService = virtualHostService;
    }

    @Reference
    public void setRewriteService( final RewriteService rewriteService )
    {
        this.rewriteService = rewriteService;
    }

}
