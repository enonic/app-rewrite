package com.enonic.app.rewrite.requesttester;

public interface VirtualHostConfig
{
    boolean isEnabled();

    VirtualHostMappings getMappings();
}
