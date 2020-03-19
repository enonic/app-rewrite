package com.enonic.app.rewrite.requesttester;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

public final class VirtualHostMappings
    implements Iterable<VirtualHostMapping>
{
    private final Set<VirtualHostMapping> set;

    public VirtualHostMappings()
    {
        this.set = new TreeSet<>();
    }

    public void add( final VirtualHostMapping mapping )
    {
        this.set.add( mapping );
    }

    public VirtualHostMapping resolve( final HttpServletRequest req )
    {
        for ( final VirtualHostMapping entry : this.set )
        {
            System.out.println( "Checking entry: " + entry );

            if ( entry.matches( req ) )
            {
                return entry;
            }
        }

        return null;
    }

    @Override
    public Iterator<VirtualHostMapping> iterator()
    {
        return this.set.iterator();
    }
}
