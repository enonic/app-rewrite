package com.enonic.app.rewrite;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.ie.ImportResult;
import com.enonic.app.rewrite.ie.ImportService;
import com.enonic.app.rewrite.mapping.ErrorMapper;
import com.enonic.app.rewrite.mapping.ImportResultMapper;
import com.enonic.app.rewrite.mapping.ProviderInfoMapper;
import com.enonic.app.rewrite.mapping.RequestTesterResultMapper;
import com.enonic.app.rewrite.mapping.RewriteConfigurationsMapper;
import com.enonic.app.rewrite.mapping.RewriteContextMapper;
import com.enonic.app.rewrite.mapping.RewriteMappingMapper;
import com.enonic.app.rewrite.mapping.VirtualHostsMapper;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.requesttester.RequestTester;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.xp.script.bean.BeanContext;
import com.enonic.xp.script.bean.ScriptBean;
import com.enonic.xp.util.Exceptions;
import com.enonic.xp.web.vhost.VirtualHost;
import com.enonic.xp.web.vhost.VirtualHostService;

public class RewriteBean
    implements ScriptBean
{
    private static final Logger LOG = LoggerFactory.getLogger( RewriteBean.class );

    private Supplier<RewriteService> rewriteServiceSupplier;

    private Supplier<RequestTester> requestTesterSupplier;

    private Supplier<VirtualHostService> virtualHostServiceSupplier;

    private Supplier<ImportService> importServiceSupplier;

    @Override
    public void initialize( final BeanContext context )
    {
        this.rewriteServiceSupplier = context.getService( RewriteService.class );
        this.requestTesterSupplier = context.getService( RequestTester.class );
        this.virtualHostServiceSupplier = context.getService( VirtualHostService.class );
        this.importServiceSupplier = context.getService( ImportService.class );
    }


    @SuppressWarnings("unused")
    public Object getRewriteConfigurations()
    {
        final Map<RewriteContextKey, Optional<RewriteMappingProvider>> rewriteConfigurations =
            this.rewriteServiceSupplier.get().getRewriteConfigurations();
        return new RewriteConfigurationsMapper( rewriteConfigurations );
    }

    public Object getRewriteContext( final String contextKey )
    {
        final VirtualHostsDecorator virtualHostAsMap = this.rewriteServiceSupplier.get().getVirtualHostMappings();

        final VirtualHostWrapper virtualHostWrapper = virtualHostAsMap.getVirtualHostMapping( RewriteContextKey.from( contextKey ) );

        return new RewriteContextMapper( virtualHostWrapper );
    }

    public Object getRewriteMapping( final String contextKey )
    {
        final RewriteMapping rewriteMapping = this.rewriteServiceSupplier.get().getRewriteMapping( RewriteContextKey.from( contextKey ) );

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
            requestTesterResult = this.requestTesterSupplier.get().testRequest( requestURI );
        }
        catch ( Exception e )
        {
            LOG.error( "RewriteBean#requestTester", e );
            return new ErrorMapper( e );
        }

        return new RequestTesterResultMapper( requestTesterResult );
    }

    public Object getVirtualHosts()
    {
        final List<VirtualHost> virtualHosts = this.virtualHostServiceSupplier.get().getVirtualHosts();

        return new VirtualHostsMapper( virtualHosts );
    }

    public Object createRewriteContext( final String contextKey )
    {
        this.rewriteServiceSupplier.get().create( RewriteContextKey.from( contextKey ) );
        return null;
    }

    public Object deleteRewriteContext( final String contextKey )
    {
        this.rewriteServiceSupplier.get().delete( RewriteContextKey.from( contextKey ) );
        return null;
    }

    public Object saveRule( final UpdateRuleParams params )
    {
        try
        {
            if ( !this.requestTesterSupplier.get().hasLoops( params ) )
            {
                this.rewriteServiceSupplier.get().saveRule( params );
                return null;
            }

            return new ErrorMapper(
                Exceptions.newRuntime( "The loop was found for the rule with source=\"{0}\" and target=\"{1}\"", params.getSource(),
                                       params.getTarget() ).withoutCause() );
        }
        catch ( Exception e )
        {
            return new ErrorMapper( e );
        }
    }

    public Object deleteRule( final DeleteRuleParams params )
    {
        this.rewriteServiceSupplier.get().deleteRule( params );
        return null;
    }

    public Object getProviderInfo( final String contextKey )
    {
        return new ProviderInfoMapper( this.rewriteServiceSupplier.get().getProviderInfo( RewriteContextKey.from( contextKey ) ) );
    }

    public Object importRules( final ImportRulesParams params )
    {
        final ImportResult importResult = this.importServiceSupplier.get().importRules( params );
        return new ImportResultMapper( importResult );
    }

    public Object serializeRules( final ExportRulesParams params )
    {
        return this.importServiceSupplier.get().serializeRules( params );
    }

}
