package com.enonic.app.rewrite.vhost;


public interface VirtualHostConfig
{
    boolean isEnabled();

    VirtualHostMappings getMappings();
}
