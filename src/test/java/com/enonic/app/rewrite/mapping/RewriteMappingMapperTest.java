package com.enonic.app.rewrite.mapping;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RedirectType;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
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