package com.enonic.app.rewrite.mapping;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.domain.RewriteRule;
import com.enonic.app.rewrite.domain.RewriteRules;
import com.enonic.app.rewrite.redirect.RedirectType;
import com.enonic.xp.script.serializer.JsonMapGenerator;

class RewriteMappingsMapperTest
{
    @Test
    void name()
    {
        final List<RewriteMapping> rewriteMappings = List.of( RewriteMapping.create().
            contextKey( new RewriteContextKey( "vhost1" ) ).
            rewriteRules( RewriteRules.create().
                addRule( RewriteRule.create().
                    type( RedirectType.MOVED_PERMANENTLY ).
                    from( "from1" ).
                    target( "target1" ).
                    build() ).
                build() ).
            build() );

        final RewriteMappingsMapper rewriteMappingsMapper = new RewriteMappingsMapper( rewriteMappings, new ArrayList<>() );
        final JsonMapGenerator gen = new JsonMapGenerator();
        rewriteMappingsMapper.serialize( gen );
    }
}
