package com.enonic.app.rewrite.mapping;

import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
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
        gen.map( "mapping" );
        gen.value( "contextKey", rewriteMapping.getContextKey().toString() );
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
        gen.value( "ruleId", rule.getRuleId() );

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
