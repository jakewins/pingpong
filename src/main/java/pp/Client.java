/**
 * Copyright (c) 2002-2014 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package pp;

import org.HdrHistogram.Histogram;
import pp.serialization.Serialization;
import pp.transport.Transport;
import pp.transport.Transports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static java.lang.Integer.parseInt;
import static pp.transport.Transport.Connection;

public class Client
{
    private final long secondsToRun;

    private final Transport transport;
    private final Serialization serialization;
    private final String host;
    private final int port;

    private long idGen = 0;

    public Client( Transport transport, Serialization serialization, String host, int port,
            long secondsToRun ) throws IOException
    {
        this.transport = transport;
        this.serialization = serialization;
        this.host = host;
        this.port = port;
        this.secondsToRun = secondsToRun;
    }

    public static void main(String ... args) throws IOException
    {
        if(args.length != 4)
        {
            System.out.println("Usage: java -jar ping.jar <transport> <serializer> <host:port> <runtimeseconds>");
            return;
        }
        Transport transport = Transports.valueOf( args[0].toUpperCase() );
        Serialization serialization = Serialization.valueOf( args[1].toUpperCase() );
        new Client( transport, serialization, args[2].split( ":" )[0], parseInt( args[2].split( ":" )[1] ),
                Long.valueOf( args[3] ) ).run();
    }

    private void run() throws IOException
    {
        Histogram histogram = new Histogram(3600000000000L, 3);
        try(Connection conn = transport.connect( host, port, serialization ))
        {
            System.out.println( "[Client] Connected, warming up" );
            hammer( conn, histogram, 5 );
            histogram.reset();

            System.out.println( "[Client] Running load.." );
            hammer( conn, histogram, secondsToRun );

            storeResults( histogram );
        }
    }

    private void hammer( Connection conn, Histogram histogram, long runtime) throws IOException
    {
        long deadline = System.currentTimeMillis() + (runtime * 1_000);
        do
        {
            sendAndRecieveOneMessage(conn, histogram);
        }
        while ( System.currentTimeMillis() < deadline );
    }

    private void sendAndRecieveOneMessage(Connection conn, Histogram histogram ) throws IOException
    {
        long startTime = System.nanoTime();
        conn.send( idGen++ );
        conn.recieve();
        histogram.recordValue( System.nanoTime() - startTime);
    }

    private void storeResults( Histogram histogram ) throws IOException
    {
        File outFile = new File(String.format( "%s-%s-%ds-%s.histogram", transport, serialization, secondsToRun,
                System.currentTimeMillis() ));
        try(FileOutputStream fso = new FileOutputStream( outFile );
            PrintStream ps = new PrintStream( fso ))
        {
            System.out.println( "Recorded latencies [in usec] for single message request/response" );
            histogram.getHistogramData().outputPercentileDistribution( System.out, 1000.0 );

            System.out.println("Stored output in: " + outFile.getAbsolutePath());
            histogram.getHistogramData().outputPercentileDistribution( ps, 1000.0 );
        }
    }
}
