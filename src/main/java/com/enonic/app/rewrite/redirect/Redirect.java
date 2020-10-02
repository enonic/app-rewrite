package com.enonic.app.rewrite.redirect;

import java.util.Objects;

public class Redirect
{
    private RedirectTarget redirectTarget;

    private RedirectType type;

    public Redirect( final RedirectTarget redirectTarget, final RedirectType type )
    {
        Objects.requireNonNull( redirectTarget, "redirectTarget cannot be null" );
        Objects.requireNonNull( type, String.format( "type cannot be null for target [%s]", redirectTarget ) );

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

    @Override
    public String toString()
    {
        return "Redirect{" + "redirectTarget=" + redirectTarget + ", type=" + type + '}';
    }

    @Override
    public boolean equals( final Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( o == null || getClass() != o.getClass() )
        {
            return false;
        }
        final Redirect redirect = (Redirect) o;
        return Objects.equals( redirectTarget, redirect.redirectTarget ) && type == redirect.type;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( redirectTarget, type );
    }
}

