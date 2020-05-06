package com.enonic.app.rewrite;

import com.enonic.app.rewrite.mapping.ErrorMapper;
import com.enonic.app.rewrite.mapping.RequestTesterResultMapper;
import com.enonic.app.rewrite.mapping.RewriteMappingsMapper;
import com.enonic.app.rewrite.requesttester.RequestTester;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
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
        final RewriteMappings rewriteMappings = this.rewriteService.getRewriteMappings();

        return new RewriteMappingsMapper( rewriteMappings );
    }

    public Object requestTester( final String requestURI )
    {
        final RequestTesterResult requestTesterResult;
        try
        {
            requestTesterResult = this.requestTester.testRequest( requestURI );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return new ErrorMapper( e );
        }

        return new RequestTesterResultMapper( requestTesterResult );
    }

}
