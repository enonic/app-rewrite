package com.enonic.app.rewrite.provider;


import com.enonic.app.rewrite.CreateRuleParams;
import com.enonic.app.rewrite.rewrite.RewriteContextKey;
import com.enonic.app.rewrite.rewrite.RewriteMapping;
import com.enonic.app.rewrite.rewrite.RewriteRule;

public interface RewriteMappingProvider
{
    RewriteMapping getRewriteMapping( final RewriteContextKey contextKey );

    void store( final RewriteMapping rewriteMapping );

    void create( final RewriteContextKey rewriteContext );

    void delete( final RewriteContextKey rewriteContextKey );

    void createRule( final CreateRuleParams params );

    boolean readOnly();

    String name();

}
