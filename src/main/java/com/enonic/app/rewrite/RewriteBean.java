package com.enonic.app.rewrite;

import com.enonic.app.rewrite.context.VHostContextHelper;
import com.enonic.app.rewrite.mapping.ErrorMapper;
import com.enonic.app.rewrite.mapping.RequestTesterResultMapper;
import com.enonic.app.rewrite.mapping.RewriteMappingsMapper;
import com.enonic.app.rewrite.mapping.VirtualHostsMapper;
import com.enonic.app.rewrite.requesttester.RequestTester;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.app.rewrite.requesttester.VirtualHostMappings;
import com.enonic.app.rewrite.rewrite.RewriteMappings;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public class RewriteBean
    implements ScriptBean
{
    private RewriteService rewriteService;

    private RequestTester requestTester;

    private VHostContextHelper vHostContextHelper;

    @Override
    public void initialize( final BeanContext context )
    {
        this.rewriteService = context.getService( RewriteService.class ).get();
        this.requestTester = context.getService( RequestTester.class ).get();
        this.vHostContextHelper = context.getService( VHostContextHelper.class ).get();
    }

    public Object getRewriteMappings()
    {
        final RewriteMappings rewriteMappings = this.rewriteService.getRewriteMappings();

        rewriteMappings.forEach( mapping -> {

        } );

        return new RewriteMappingsMapper( rewriteMappings, this.vHostContextHelper.getMappings() );
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

    public Object getVirtualHosts()
    {
        final VirtualHostMappings virtualHosts = this.vHostContextHelper.getMappings();

        return new VirtualHostsMapper( virtualHosts );
    }

}
