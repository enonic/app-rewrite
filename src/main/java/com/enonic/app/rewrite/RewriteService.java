package com.enonic.app.rewrite;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;

import com.enonic.app.rewrite.engine.RewriteRulesLoadResult;
import com.enonic.app.rewrite.provider.ProviderInfo;
import com.enonic.app.rewrite.provider.RewriteMappingProvider;
import com.enonic.app.rewrite.redirect.RedirectMatch;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;
import com.enonic.app.rewrite.vhost.RewriteConfigurations;
import com.enonic.app.rewrite.vhost.VirtualHostMapping;

public interface RewriteService
{
    RedirectMatch process( final HttpServletRequest request );

    RewriteMapping getRewriteMapping( final RewriteContextKey rewriteContextKey );

    ConcurrentMap<RewriteContextKey, Optional<RewriteMappingProvider>> getRewriteConfigurations();

    VirtualHostMapping getRewriteContext( final RewriteContextKey contextKey );

    void store( final RewriteMapping rewriteMapping );

    void createRule( final CreateRuleParams params );

    void deleteRule( final DeleteRuleParams params );

    void create( final RewriteContextKey rewriteContextKey );

    void delete( final RewriteContextKey rewriteContextKey );

    ProviderInfo getProviderInfo( final RewriteContextKey rewriteContextKey );

    RewriteRulesLoadResult load();

}
