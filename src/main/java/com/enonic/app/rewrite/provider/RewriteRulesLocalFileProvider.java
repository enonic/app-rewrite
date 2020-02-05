package com.enonic.app.rewrite.provider;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tuckey.web.filters.urlrewrite.Conf;

class RewriteRulesLocalFileProvider
    implements RewriteRulesProvider
{
    private final Conf config;

    private final Logger LOG = LoggerFactory.getLogger( RewriteRulesLocalFileProvider.class );

    RewriteRulesLocalFileProvider( final String rulesFilePath )
    {
        try
        {
            final File configFile = new File( rulesFilePath.toString() );

            if ( configFile.exists() )
            {
                LOG.info( "Fetching rewrite-config from file: [{}]", rulesFilePath );
                FileInputStream fileStream = new FileInputStream( configFile );
                this.config = new Conf( fileStream, rulesFilePath );
            }
            else
            {
                throw new RuntimeException(
                    String.format( "Cannot configure rewrite-filter, config-file: [%s] does not exist", rulesFilePath ) );
            }
        }
        catch ( FileNotFoundException e )
        {
            throw new RuntimeException( "Cannot configure rewrite-filter" );
        }
    }

    @Override
    public Conf get()
    {
        return this.config;
    }


}
