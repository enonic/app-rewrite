package com.enonic.app.rewrite.vhost;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import com.enonic.xp.web.vhost.VirtualHost;

@Component(immediate = true)
public class VirtualHostResolverImpl
    implements VirtualHostResolver
{
    private final static Logger LOG = LoggerFactory.getLogger( VirtualHostResolverImpl.class );

    private VirtualHostConfig virtualHostConfig;

    private final Map<String, VirtualHost> vhostMappings = Maps.newHashMap();

    @Activate
    public void activate()
    {
        LOG.info( "Fetching vhost-configs" );
        this.virtualHostConfig.getMappings().forEach( vhostMapping -> {
            vhostMappings.put( vhostMapping.getName(), vhostMapping );
        } );
    }

    @Override
    public VirtualHost resolve( final String name )
    {
        return this.vhostMappings.get( name );
    }

    @Reference
    public void setVirtualHostConfig( final VirtualHostConfig virtualHostConfig )
    {
        this.virtualHostConfig = virtualHostConfig;
    }
}
