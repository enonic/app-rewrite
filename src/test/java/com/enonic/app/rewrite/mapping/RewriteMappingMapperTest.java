package com.enonic.app.rewrite.mapping;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
import com.enonic.xp.script.serializer.JsonMapGenerator;

class RewriteMappingMapperTest
{


    @Test
    void name()
    {
        final RewriteMapping.Builder builder = RewriteMapping.create();
        builder.add( new RewriteContextKey( "vhost1" ), RewriteRules.create().
            addRule( RewriteRule.create().
                type( RedirectType.MOVED_PERMANENTLY ).
                from( "from1" ).
                target( "target1" ).
                build() ).
            build() );

        final RewriteMappingMapper rewriteMappingMapper = new RewriteMappingMapper( builder.build() );
        final JsonMapGenerator gen = new JsonMapGenerator();
        rewriteMappingMapper.serialize( gen );
    }
}