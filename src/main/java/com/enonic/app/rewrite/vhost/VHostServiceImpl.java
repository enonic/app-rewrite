package com.enonic.app.rewrite.vhost;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class VHostServiceImpl
    implements VHostService
{
    private VirtualHostConfig virtualHostConfig;

    private VirtualHostMappings virtualHostMappings;

    @Activate
    public void activate()
    {
        this.virtualHostMappings = virtualHostConfig.getMappings();
    }

    @Reference
    public void setVirtualHostConfig( final VirtualHostConfig virtualHostConfig )
    {
        this.virtualHostConfig = virtualHostConfig;
    }

    @Override
    public VirtualHostMapping getMapping( final String name )
    {
        for ( final VirtualHostMapping mapping : this.virtualHostMappings )
        {
            if ( mapping.getName().equals( name ) )
            {
                return mapping;
            }
        }

        return null;
    }

    @Override
    public VirtualHostMappings getMappings()
    {
        return this.virtualHostMappings;
    }

}
