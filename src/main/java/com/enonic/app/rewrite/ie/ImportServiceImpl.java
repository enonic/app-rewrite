package com.enonic.app.rewrite.ie;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.ExportRulesParams;
import com.enonic.app.rewrite.ImportRulesParams;
import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.format.SourceFormat;
import com.enonic.app.rewrite.format.SourceFormatResolver;
import com.enonic.app.rewrite.format.SourceReadResult;
import com.enonic.app.rewrite.format.SourceReader;
import com.enonic.app.rewrite.format.SourceWriter;
import com.enonic.app.rewrite.domain.RewriteMapping;

@Component(enabled = false)
public class ImportServiceImpl
    implements ImportService
{
    private final static Logger LOG = LoggerFactory.getLogger( ImportService.class );

    private RewriteService rewriteService;

    @Override
    public String serializeRules( final ExportRulesParams params )
    {
        LOG.info( "Exporting rules from " + params.getContextKey() );
        final RewriteMapping rewriteMapping = this.rewriteService.getRewriteMapping( params.getContextKey() );
        return SourceWriter.serialize( rewriteMapping.getRewriteRules(), params.getFormat() );
    }

    @Override
    public ImportResult importRules( final ImportRulesParams params )
    {
        LOG.info( "Importing rules into " + params.getContextKey() + ", dryRun: " + params.isDryRun() );

        try (final BufferedReader reader = params.getByteSource().asCharSource( StandardCharsets.UTF_8 ).openBufferedStream())
        {
            final SourceReadResult result = SourceReader.read( reader, params.getFormat() != null
                ? SourceFormat.get( params.getFormat() )
                : SourceFormatResolver.resolve( params.getFileName() ) );
            return handleStrategy( params, result );
        }
        catch ( IOException e )
        {
            return ImportResult.failed();
        }
    }

    private ImportResult handleStrategy( final ImportRulesParams params, final SourceReadResult result )
    {
        //if ( params.getMergeStrategy().equals( "delete" ) )
        //{
        return handleDeleteStrategy( params, result );
        // }
    }

    private ImportResult handleDeleteStrategy( final ImportRulesParams params, final SourceReadResult result )
    {
        final ImportResult.Builder builder = ImportResult.create();

        final RewriteMapping previous = this.rewriteService.getRewriteMapping( params.getContextKey() );
        builder.setDeleted( previous.getRewriteRules().size() );

        if ( params.isDryRun() )
        {
            LOG.info( "Dry-run, no changes done to repo" );
        }
        else
        {
            this.rewriteService.store( RewriteMapping.create().
                rewriteRules( result.getRules() ).
                contextKey( params.getContextKey() ).
                build() );
        }
        builder.setNew( result.getOk() );
        builder.setUnsupported( result.getUnsupported() );
        builder.setErrors( result.getFailed() );

        return builder.build();
    }

    @Reference
    public void setRewriteService( final RewriteService rewriteService )
    {
        this.rewriteService = rewriteService;
    }
}
