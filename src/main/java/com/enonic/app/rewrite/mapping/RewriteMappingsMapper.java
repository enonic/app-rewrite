package com.enonic.app.rewrite.mapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;
import com.enonic.xp.web.vhost.VirtualHost;

public class RewriteMappingsMapper
    implements MapSerializable
{
    private final List<RewriteMapping> rewriteMappings;

    private final Map<String, VirtualHost> virtualHostMappingMap = new HashMap<>();

    public RewriteMappingsMapper( final List<RewriteMapping> rewriteMappings, final List<VirtualHost> virtualHostMappings )
    {
        this.rewriteMappings = rewriteMappings;

        virtualHostMappings.forEach( mapping -> this.virtualHostMappingMap.put( mapping.getName(), mapping ) );
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        this.rewriteMappings.forEach( mapping -> mapMapping( gen, mapping ) );
    }

    private void mapMapping( final MapGenerator gen, final RewriteMapping rewriteMapping )
    {
        gen.map( rewriteMapping.getContextKey().toString() );
        mapContext( gen, rewriteMapping );
        gen.end();
    }

    private void mapContext( final MapGenerator gen, final RewriteMapping rewriteMapping )
    {
        final VirtualHost virtualHostMapping = this.virtualHostMappingMap.get( rewriteMapping.getContextKey().toString() );
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

        mapRules( gen, rewriteMapping.getRewriteRules() );

        gen.end();
    }

    private void mapRules( final MapGenerator gen, final RewriteRules rewriteRules )
    {
        gen.array( "rules" );
        int i = 0;
        for ( final RewriteRule rule : rewriteRules.getRuleList() )
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
