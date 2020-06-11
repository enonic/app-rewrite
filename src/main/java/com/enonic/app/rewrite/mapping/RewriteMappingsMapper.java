package com.enonic.app.rewrite.mapping;

import java.util.Map;

import com.google.common.collect.Maps;

import com.enonic.app.rewrite.vhost.VirtualHostMapping;
import com.enonic.app.rewrite.vhost.VirtualHostMappings;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class RewriteMappingsMapper
    implements MapSerializable
{
    private final RewriteMappings rewriteMappings;

    private final Map<String, VirtualHostMapping> virtualHostMappingMap = Maps.newHashMap();

    public RewriteMappingsMapper( final RewriteMappings rewriteMappings, final VirtualHostMappings virtualHostMappings )
    {
        this.rewriteMappings = rewriteMappings;

        virtualHostMappings.forEach( mapping -> {
            this.virtualHostMappingMap.put( mapping.getName(), mapping );
        } );
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        this.rewriteMappings.forEach( mapping -> {
            mapMapping( gen, mapping );
        } );
    }

    private void mapMapping( final MapGenerator gen, final RewriteMapping rewriteMapping )
    {
        gen.map( rewriteMapping.getContextKey().toString() );
        mapContext( gen, rewriteMapping );
        gen.end();
    }

    private void mapContext( final MapGenerator gen, final RewriteMapping rewriteMapping )
    {
        final VirtualHostMapping virtualHostMapping = this.virtualHostMappingMap.get( rewriteMapping.getContextKey().toString() );
        if ( virtualHostMapping != null )
        {
            gen.value( "type", "virtualHost" );
            gen.value( "host", virtualHostMapping.getHost() );
            gen.value( "source", virtualHostMapping.getSource() );
            gen.value( "target", virtualHostMapping.getTarget() );
            gen.value( "defaultIdProviderKey", virtualHostMapping.getDefaultIdProviderKey() );
        }
        else
        {
            gen.value( "type", "not found" );
        }

        mapRules( gen, rewriteMapping );

        gen.end();
    }

    private void mapRules( final MapGenerator gen, final Iterable<RewriteRule> rules )
    {
        gen.array( "rules" );
        int i = 0;
        for ( final RewriteRule rule : rules )
        {
            mapRule( gen, rule, i++ );
        }

        gen.end();
    }

    private void mapRule( final MapGenerator gen, final RewriteRule rule, int index )
    {
        gen.map();
        gen.value( "order", index );
        gen.value( "from", rule.getFrom() );
        mapTarget( gen, rule );
        gen.value( "type", rule.getType() );

        gen.end();
    }

    private void mapTarget( final MapGenerator gen, final RewriteRule rule )
    {
        gen.map( "target" );
        gen.value( "path", rule.getTarget().path() );
        gen.value( "external", rule.getTarget().isExternal() );
        gen.end();
    }
}
