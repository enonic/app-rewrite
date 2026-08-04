package com.enonic.app.rewrite;

import com.enonic.app.rewrite.domain.RewriteContextKey;
import com.enonic.app.rewrite.format.SourceFormat;
import com.enonic.app.rewrite.ie.ImportResult;
import com.enonic.app.rewrite.ie.ImportService;
import com.enonic.app.rewrite.requesttester.RequestTester;
import com.enonic.app.rewrite.requesttester.RequestTesterResult;
import com.enonic.xp.testing.ScriptRunnerSupport;
import com.enonic.xp.web.vhost.VirtualHostService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RewriteDaoScriptTest
    extends ScriptRunnerSupport
{
    private UpdateRuleParams savedRule;

    private DeleteRuleParams deletedRule;

    private ImportRulesParams importedRules;

    private ExportRulesParams exportedRules;

    private RequestTesterParams testedRequest;

    @Override
    protected void initialize()
        throws Exception
    {
        super.initialize();

        final RewriteService rewriteService = mock( RewriteService.class );
        final RequestTester requestTester = mock( RequestTester.class );
        final ImportService importService = mock( ImportService.class );
        final VirtualHostService virtualHostService = mock( VirtualHostService.class );

        when( requestTester.hasLoops( any() ) ).thenReturn( false );

        doAnswer( invocation -> {
            this.savedRule = invocation.getArgument( 0 );
            return null;
        } ).when( rewriteService ).saveRule( any() );

        doAnswer( invocation -> {
            this.deletedRule = invocation.getArgument( 0 );
            return null;
        } ).when( rewriteService ).deleteRule( any() );

        when( importService.importRules( any() ) ).thenAnswer( invocation -> {
            this.importedRules = invocation.getArgument( 0 );
            return ImportResult.create().build();
        } );

        when( importService.serializeRules( any() ) ).thenAnswer( invocation -> {
            this.exportedRules = invocation.getArgument( 0 );
            return "SERIALIZED_RULES";
        } );

        when( requestTester.testRequest( any() ) ).thenAnswer( invocation -> {
            this.testedRequest = invocation.getArgument( 0 );
            return RequestTesterResult.create().build();
        } );

        addService( RewriteService.class, rewriteService );
        addService( RequestTester.class, requestTester );
        addService( ImportService.class, importService );
        addService( VirtualHostService.class, virtualHostService );
    }

    @Override
    public String getScriptTestFile()
    {
        return "/lib/rewrite-dao-test.js";
    }

    public String getSavedHost()
    {
        return this.savedRule.getHost();
    }

    public String getSavedInsertStrategy()
    {
        return this.savedRule.getInsertStrategy();
    }

    public String getSavedSource()
    {
        return this.savedRule.getSource();
    }

    public String getSavedTarget()
    {
        return this.savedRule.getTarget();
    }

    public String getSavedType()
    {
        return this.savedRule.getType();
    }

    public String getSavedContextKey()
    {
        return asString( this.savedRule.getContextKey() );
    }

    public Integer getSavedPosition()
    {
        return this.savedRule.getPosition();
    }

    public String getSavedRuleId()
    {
        return this.savedRule.getRuleId();
    }

    public String getDeletedContextKey()
    {
        return asString( this.deletedRule.getContextKey() );
    }

    public String getDeletedRuleId()
    {
        return this.deletedRule.getRuleId();
    }

    public String getImportedContextKey()
    {
        return asString( this.importedRules.getContextKey() );
    }

    public String getImportedMergeStrategy()
    {
        return this.importedRules.getMergeStrategy();
    }

    public String getImportedFileName()
    {
        return this.importedRules.getFileName();
    }

    public boolean getImportedDryRun()
    {
        return this.importedRules.isDryRun();
    }

    public String getImportedFormat()
    {
        return this.importedRules.getFormat();
    }

    public String getExportedContextKey()
    {
        return asString( this.exportedRules.getContextKey() );
    }

    public String getExportedFormat()
    {
        final SourceFormat format = this.exportedRules.getFormat();
        return format == null ? null : format.name();
    }

    public String getTestedHost()
    {
        return this.testedRequest.getHost();
    }

    public String getTestedRequestPath()
    {
        return this.testedRequest.getRequestPath();
    }

    public String getTestedRewriteContext()
    {
        return this.testedRequest.getRewriteContext();
    }

    private static String asString( final RewriteContextKey contextKey )
    {
        return contextKey == null ? null : contextKey.toString();
    }
}
