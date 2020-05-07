package com.enonic.app.rewrite.context;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.enonic.app.rewrite.requesttester.VirtualHostConfig;
import com.enonic.app.rewrite.requesttester.VirtualHostMapping;
import com.enonic.app.rewrite.requesttester.VirtualHostMappings;

@Component(immediate = true)
public class VHostContextHelperImpl
    implements VHostContextHelper
{
    private VirtualHostConfig virtualHostConfig;

    private VirtualHostMappings virtualHostMappings;

    @Activate
    public void activate()
    {
        this.virtualHostMappings = virtualHostConfig.getMappings();

        this.virtualHostConfig.getMappings().forEach( mapping -> {
            System.out.println( "############## VHOSTNAME: " + mapping.getName() );
        } );
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
