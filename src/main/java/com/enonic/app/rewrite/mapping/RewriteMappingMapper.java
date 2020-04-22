package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
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

            System.out.println( "MapKey: " + key );

            gen.map( key.toString() );
            mapRules( gen, rules );
            gen.end();
        } );
    }

    private void mapRules( final MapGenerator gen, final RewriteRules rules )
    {
        gen.array( "rules" );
        rules.forEach( rule -> {
            mapRule( gen, rule );
        } );
        gen.end();
    }

    private void mapRule( final MapGenerator gen, final RewriteRule rule )
    {
        gen.map();
        gen.value( "order", rule.getOrder() );
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
