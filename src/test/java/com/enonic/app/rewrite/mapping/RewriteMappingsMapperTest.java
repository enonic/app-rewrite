package com.enonic.app.rewrite.mapping;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.app.rewrite.vhost.VirtualHostMappings;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.rewrite.RewriteRules;
import com.enonic.xp.script.serializer.JsonMapGenerator;

class RewriteMappingsMapperTest
{
    @Test
    void name()
    {
        final RewriteMappings.Builder builder = RewriteMappings.create();
        builder.add( RewriteMapping.create().
            contextKey( new RewriteContextKey( "vhost1" ) ).
            rewriteRules( RewriteRules.create().
                addRule( RewriteRule.create().
                    type( RedirectType.MOVED_PERMANENTLY ).
                    from( "from1" ).
                    target( "target1" ).
                    build() ).
                build() ).
            build() );

        final RewriteMappingsMapper rewriteMappingsMapper = new RewriteMappingsMapper( builder.build(), new VirtualHostMappings() );
        final JsonMapGenerator gen = new JsonMapGenerator();
        rewriteMappingsMapper.serialize( gen );
    }
}