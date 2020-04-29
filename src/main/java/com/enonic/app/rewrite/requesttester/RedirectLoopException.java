package com.enonic.app.rewrite.requesttester;

import java.util.List;

import com.enonic.app.rewrite.redirect.Redirect;

public class RedirectLoopException
    extends RuntimeException
{
    public RedirectLoopException( final List<Redirect> targets, final Redirect loopRedirect )
    {
        super( String.format( "Detected redirect loop, previous targets: %s , next target %s", targets, loopRedirect ) );
    }
}
