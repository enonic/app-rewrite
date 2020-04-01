package com.enonic.app.rewrite.repo.model;

import com.enonic.xp.node.Node;

public class VHost
{
    private static final String VHOST_PROPERTY_NAME = "vhostName";

    private final String name;

    public VHost( final String name )
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public static VHost fromNode( final Node node )
    {

        final String name = node.data().getString( VHOST_PROPERTY_NAME );

        return new VHost( name );
    }


}
