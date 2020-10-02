package com.enonic.app.rewrite.format;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.google.common.io.ByteSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVSerializerTest
{

    @Test
    void name()
        throws Exception
    {
        final String testData = "From,Target,Type\n" + "\"/fisk\",\"/ost\",302\n" + "\"/fisk\",\"/ost\",";

        final SourceReadResult result =
            CSVSerializer.read( ByteSource.wrap( testData.getBytes() ).asCharSource( StandardCharsets.UTF_8 ).openBufferedStream() );

        assertEquals( 2, result.getOk() );
    }
}
