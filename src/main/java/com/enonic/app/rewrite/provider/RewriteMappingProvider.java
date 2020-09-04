package com.enonic.app.rewrite.provider;


import com.enonic.app.rewrite.CreateRuleParams;
import com.enonic.app.rewrite.DeleteRuleParams;
import com.enonic.app.rewrite.EditRuleParams;
import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.domain.RewriteMapping;

public interface RewriteMappingProvider
{
    RewriteMapping getRewriteMapping( final RewriteContextKey contextKey );

    boolean providesForContext( final RewriteContextKey contextKey );

    void store( final RewriteMapping rewriteMapping );

    void create( final RewriteContextKey rewriteContext );

    void delete( final RewriteContextKey rewriteContextKey );

    void editRule( final EditRuleParams editRuleParams );

    void createRule( final CreateRuleParams params );

    void deleteRule( final DeleteRuleParams params );

    boolean readOnly();

    String name();

}
