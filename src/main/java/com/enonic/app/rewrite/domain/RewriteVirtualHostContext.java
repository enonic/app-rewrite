package com.enonic.app.rewrite.domain;

import com.enonic.xp.web.vhost.VirtualHost;

public class RewriteVirtualHostContext
    implements RewriteContext
{
    private final RewriteContextKey key;

    private final String sourceContext;

    private final String targetContext;

    public RewriteVirtualHostContext( final VirtualHost virtualHost )
    {
        this.key = RewriteContextKey.from( virtualHost.getName() );
        this.sourceContext = virtualHost.getSource();
        this.targetContext = virtualHost.getTarget();
    }

    @Override
    public RewriteContextKey getKey()
    {
        return key;
    }

    @Override
    public String getSourceContext()
    {
        return sourceContext;
    }

    @Override
    public String getTargetContext()
    {
        return targetContext;
    }
}
