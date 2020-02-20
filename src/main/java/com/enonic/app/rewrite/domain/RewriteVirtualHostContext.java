package com.enonic.app.rewrite.domain;

import com.enonic.xp.web.vhost.VirtualHost;

public class RewriteVirtualHostContext
    implements RewriteContext
{
    private VirtualHost virtualHost;

    public RewriteVirtualHostContext( final VirtualHost virtualHost )
    {
        this.virtualHost = virtualHost;
    }

    @Override
    public String context()
    {
        return this.virtualHost.getTarget();
    }
}
