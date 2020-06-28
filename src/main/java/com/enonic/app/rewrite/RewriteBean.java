package com.enonic.app.rewrite;

import com.enonic.app.rewrite.mapping.ErrorMapper;
import com.enonic.app.rewrite.mapping.ProviderInfoMapper;
import com.enonic.app.rewrite.mapping.RequestTesterResultMapper;
import com.enonic.app.rewrite.mapping.RewriteConfigurationsMapper;
import com.enonic.app.rewrite.mapping.RewriteContextMapper;
import com.enonic.app.rewrite.mapping.RewriteMappingMapper;
import com.enonic.app.rewrite.mapping.VirtualHostsMapper;
import com.enonic.app.rewrite.requesttester.RequestTester;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.vhost.RewriteConfigurations;
import com.enonic.app.rewrite.vhost.VHostService;
import com.enonic.app.rewrite.vhost.VirtualHostMapping;
import com.enonic.app.rewrite.vhost.VirtualHostMappings;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;

public class RewriteBean
    implements ScriptBean
{
    private RewriteService rewriteService;

    private RequestTester requestTester;

    private VHostService vHostService;

    @Override
    public void initialize( final BeanContext context )
    {
        this.rewriteService = context.getService( RewriteService.class ).get();
        this.requestTester = context.getService( RequestTester.class ).get();
        this.vHostService = context.getService( VHostService.class ).get();
    }


    @SuppressWarnings("unused")
    public Object getRewriteConfigurations()
    {
        final RewriteConfigurations rewriteConfigurations = this.rewriteService.getRewriteConfigurations();
        return new RewriteConfigurationsMapper( rewriteConfigurations );
    }

    public Object getRewriteContext( final String contextKey )
    {
        final VirtualHostMapping rewriteContext = this.rewriteService.getRewriteContext( new RewriteContextKey( contextKey ) );

        return new RewriteContextMapper( rewriteContext );
    }

    public Object getRewriteMapping( final String contextKey )
    {
        final RewriteMapping rewriteMapping = this.rewriteService.getRewriteMapping( new RewriteContextKey( contextKey ) );

        if ( rewriteMapping == null )
        {
            return null;
        }

        return new RewriteMappingMapper( rewriteMapping );
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
        final VirtualHostMappings virtualHosts = this.vHostService.getMappings();

        return new VirtualHostsMapper( virtualHosts );
    }

    public Object createRewriteContext( final String contextKey )
    {
        this.rewriteService.create( new RewriteContextKey( contextKey ) );
        return null;
    }

    public Object deleteRewriteContext( final String contextKey )
    {
        this.rewriteService.delete( new RewriteContextKey( contextKey ) );
        return null;
    }

    public Object createRule( final CreateRuleParams params )
    {
        this.rewriteService.createRule( params );
        return null;
    }

    public Object deleteRule( final DeleteRuleParams params )
    {
        this.rewriteService.deleteRule( params );
        return null;
    }

    public Object getProviderInfo( final String contextKey )
    {
        return new ProviderInfoMapper( this.rewriteService.getProviderInfo( RewriteContextKey.from( contextKey ) ) );
    }
}