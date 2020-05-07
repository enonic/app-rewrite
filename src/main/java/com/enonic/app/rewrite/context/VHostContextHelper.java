package com.enonic.app.rewrite.context;

import com.enonic.app.rewrite.requesttester.VirtualHostMapping;
import com.enonic.app.rewrite.requesttester.VirtualHostMappings;

public interface VHostContextHelper
{
    VirtualHostMapping getMapping( final String name );

    VirtualHostMappings getMappings();
}
