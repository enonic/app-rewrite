package com.enonic.app.rewrite.redirect;

import com.google.common.base.Preconditions;

public class Redirect
{
    private RedirectTarget redirectTarget;

    private RedirectType type;

    public Redirect( final RedirectTarget redirectTarget, final RedirectType type )
    {
        Preconditions.checkNotNull( redirectTarget, "redirectTarget cannot be null" );
        Preconditions.checkNotNull( type, "type cannot be null for target [%s]", redirectTarget );

        this.redirectTarget = redirectTarget;
        this.type = type;
    }

    public RedirectTarget getRedirectTarget()
    {
        return redirectTarget;
    }

    public RedirectType getType()
    {
        return type;
    }
}
