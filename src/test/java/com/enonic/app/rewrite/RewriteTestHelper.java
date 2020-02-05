package com.enonic.app.rewrite;

import java.io.FileInputStream;
import java.net.URL;

import org.tuckey.web.filters.urlrewrite.Conf;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RewriteTestHelper
{

    public static final Conf getConf( final String fileName )
        throws Exception
    {
        FileInputStream fileStream = new FileInputStream( getConfigFile( fileName ) );

        final Conf conf = new Conf( fileStream, getConfigFile( fileName ) );

        return conf;
    }

    private static String getConfigFile( final String fileName )
    {
        final URL configFileUrl = RewriteTestHelper.class.getResource( fileName );
        assertNotNull( configFileUrl, "File [" + fileName + "] not found" );
        return configFileUrl.getPath();
    }



}
