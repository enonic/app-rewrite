package com.enonic.app.rewrite.provider;


import com.enonic.app.rewrite.engine.RewriteEngineConfig;

public interface RewriteRulesProvider
{
    RewriteEngineConfig provide();
}
