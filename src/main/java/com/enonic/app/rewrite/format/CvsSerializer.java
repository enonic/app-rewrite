package com.enonic.app.rewrite.format;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CvsSerializer
{
    static SourceReadResult read( final BufferedReader reader )
        throws IOException
    {
        final CSVParser parser = CSVParser.parse( reader, CSVFormat.DEFAULT );
        for ( CSVRecord csvRecord : parser )
        {
            // CREATE REWRITE-RULE
        }

        return null;
    }

}
