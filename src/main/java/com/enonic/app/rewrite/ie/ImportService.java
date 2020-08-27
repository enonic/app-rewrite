package com.enonic.app.rewrite.ie;

import com.enonic.app.rewrite.ExportRulesParams;
import com.enonic.app.rewrite.ImportRulesParams;

public interface ImportService
{
    ImportResult importRules( final ImportRulesParams params );

    String serializeRules( final ExportRulesParams params );

}
