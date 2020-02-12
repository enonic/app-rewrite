package com.enonic.app.rewrite.domain;

import com.enonic.xp.web.vhost.VirtualHost;

public class RewriteVHostContext
    implements RewriteContext
{
    private VirtualHost virtualHost;

    public RewriteVHostContext( final VirtualHost virtualHost )
    {
        this.virtualHost = virtualHost;
    }

    @Override
    public String context()
    {
        return this.virtualHost.getSource();
    }
}
