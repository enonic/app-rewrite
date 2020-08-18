package com.enonic.app.rewrite.ie;

import com.enonic.app.rewrite.ImportRulesParams;

public interface ImportService
{
    ImportResult importRules( final ImportRulesParams params );
}
