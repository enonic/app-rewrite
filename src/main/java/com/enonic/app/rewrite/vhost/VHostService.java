package com.enonic.app.rewrite.vhost;

import com.enonic.app.rewrite.vhost.VirtualHostMapping;
import com.enonic.app.rewrite.vhost.VirtualHostMappings;

public interface VHostService
{
    VirtualHostMapping getMapping( final String name );

    VirtualHostMappings getMappings();
}
