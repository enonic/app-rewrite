package com.enonic.app.rewrite.vhost;

import com.enonic.xp.web.vhost.VirtualHost;

public interface VirtualHostResolver
{
    VirtualHost resolve( final String name );

}
