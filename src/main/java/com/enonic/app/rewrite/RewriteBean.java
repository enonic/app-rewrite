package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.mapping.RequestTesterResultMapper;
import com.enonic.app.rewrite.mapping.RewriteMappingMapper;
import com.enonic.app.rewrite.requesttester.RequestTester;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public class RewriteBean
    implements ScriptBean
{
    private RewriteService rewriteService;

    private RequestTester requestTester;

    @Override
    public void initialize( final BeanContext context )
    {
        this.rewriteService = context.getService( RewriteService.class ).get();
        this.requestTester = context.getService( RequestTester.class ).get();
    }

    public Object getRewriteMapping()
    {
        final RewriteMapping rewriteMapping = this.rewriteService.getRewriteMapping();

        return new RewriteMappingMapper( rewriteMapping );
    }

    public Object requestTester( final String requestURI )
    {
        final RequestTesterResult requestTesterResult = this.requestTester.testRequest( requestURI );

        return new RequestTesterResultMapper( requestTesterResult );
    }

}
