package com.enonic.app.rewrite.domain;

public class RedirectMatch
{
    private final Redirect redirect;

    private final int matchId;

    public RedirectMatch( final Redirect redirect, final int matchId )
    {
        this.redirect = redirect;
        this.matchId = matchId;
    }

    public Redirect getRedirect()
    {
        return redirect;
    }

    public int getMatchId()
    {
        return matchId;
    }
}
