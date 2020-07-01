package com.enonic.app.rewrite.io;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enonic.app.rewrite.ImportRulesParams;
import com.enonic.app.rewrite.RewriteService;
import com.enonic.app.rewrite.format.RewriteFormatReader;
import com.enonic.app.rewrite.rewrite.RewriteMapping;

@Component(immediate = true)
public class ImportServiceImpl
    implements ImportService
{
    private final static Logger LOG = LoggerFactory.getLogger( ImportService.class );

    private RewriteService rewriteService;

    public ImportResult importRules( final ImportRulesParams params )
    {
        LOG.info( "Importing rules into " + params.getContextKey() + ", dryRun: " + params.isDryRun() );

        final ReadRulesResult result = RewriteFormatReader.read( params.getByteSource() );

        return handleStrategy( params, result );
    }

    private ImportResult handleStrategy( final ImportRulesParams params, final ReadRulesResult readResult )
    {
        //if ( params.getMergeStrategy().equals( "delete" ) )
        //{
        return handleDeleteStrategy( params, readResult );
        // }
    }

    private ImportResult handleDeleteStrategy( final ImportRulesParams params, final ReadRulesResult readResult )
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
                rewriteRules( readResult.getRules() ).
                contextKey( params.getContextKey() ).
                build() );
        }
        builder.setNew( readResult.getRules().size() );

        return builder.build();
    }

    @Reference
    public void setRewriteService( final RewriteService rewriteService )
    {
        this.rewriteService = rewriteService;
    }
}
