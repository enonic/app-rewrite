package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.xp.script.serializer.MapGenerator;
import com.enonic.xp.script.serializer.MapSerializable;

public class RewriteMappingMapper
    implements MapSerializable
{
    private final RewriteMapping rewriteMapping;

    public RewriteMappingMapper( final RewriteMapping rewriteMapping )
    {
        this.rewriteMapping = rewriteMapping;
    }

    @Override
    public void serialize( final MapGenerator gen )
    {
        this.rewriteMapping.getRewriteRulesMap().forEach( ( key, rules ) -> {
            gen.map( key.toString() );
            gen.array( "rules" );
            rules.forEach( rule -> {
                gen.map();
                gen.value( "from", rule.getFrom() );
                gen.value( "to", rule.getTarget().path() );
                gen.value( "type", rule.getType() );
                gen.end();
            } );
            gen.end();
        } );
    }
}
