package com.enonic.app.rewrite;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;
import com.enonic.app.rewrite.engine.ExtRulePattern;
import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.redirect.RedirectMatch;

public interface RewriteService
{
    RedirectMatch process( final HttpServletRequest request );

    RedirectMatch process( final HttpServletRequest request, final ExtRulePattern extRulePattern );

    RewriteMapping getRewriteMapping( final RewriteContextKey rewriteContextKey );

    ConcurrentMap<RewriteContextKey, Optional<RewriteMappingProvider>> getRewriteConfigurations();

    void store( final RewriteMapping rewriteMapping );

    void saveRule( final UpdateRuleParams params );

    void deleteRule( final DeleteRuleParams params );

    void create( final RewriteContextKey rewriteContextKey );

    void delete( final RewriteContextKey rewriteContextKey );

    ProviderInfo getProviderInfo( final RewriteContextKey rewriteContextKey );

    VirtualHostsDecorator getVirtualHostMappings();

    void reloadRewriteMappings();

}
