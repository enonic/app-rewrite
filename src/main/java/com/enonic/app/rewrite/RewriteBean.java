package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.mapping.RewriteMappingMapper;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public class RewriteBean
    implements ScriptBean
{
    private RewriteService rewriteService;

    @Override
    public void initialize( final BeanContext context )
    {
        this.rewriteService = context.getService( RewriteService.class ).get();
    }


    public Object getRewriteMapping()
    {
        final RewriteMapping rewriteMapping = this.rewriteService.getRewriteMapping();

        return new RewriteMappingMapper( rewriteMapping );
    }


}
