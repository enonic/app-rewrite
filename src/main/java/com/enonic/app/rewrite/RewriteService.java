package com.enonic.app.rewrite;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.engine.RewriteRulesLoadResult;
import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.xp.web.vhost.VirtualHost;

public interface RewriteService
{
    RedirectMatch process( final HttpServletRequest request );

    RewriteMapping getRewriteMapping( final RewriteContextKey rewriteContextKey );

    ConcurrentMap<RewriteContextKey, Optional<RewriteMappingProvider>> getRewriteConfigurations();

    VirtualHost getRewriteContext( final RewriteContextKey contextKey );

    void store( final RewriteMapping rewriteMapping );

    void createRule( final CreateRuleParams params );

    void deleteRule( final DeleteRuleParams params );

    void editRule( final EditRuleParams params );

    void create( final RewriteContextKey rewriteContextKey );

    void delete( final RewriteContextKey rewriteContextKey );

    ProviderInfo getProviderInfo( final RewriteContextKey rewriteContextKey );

    RewriteRulesLoadResult load();

    VirtualHostsDecorator getVirtualHostMappings();

}
