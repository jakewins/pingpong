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

import pp.serialization.Serialization;
import pp.transport.Transport;
import pp.transport.Transports;

import java.io.IOException;

import static pp.transport.Transport.Connection;

public class Server
{
    private final Transport trans;
    private final Serialization serialization;
    private final String host;
    private final int port;

    public Server( Transport trans, Serialization serialization, String host, int port ) throws IOException
    {
        this.trans = trans;
        this.serialization = serialization;
        this.host = host;
        this.port = port;
    }

    public static void main(String ... args) throws IOException
    {
        if(args.length != 2)
        {
            System.out.println("Usage: java -jar server.jar <serializer> <transport>");
            return;
        }

        Serialization serialization = Serialization.valueOf( args[0].toUpperCase() );
        Transport trans = Transports.valueOf( args[1].toUpperCase() );

        new Server( trans, serialization, "0.0.0.0", 7474 ).run();
    }

    private void run() throws IOException
    {
        System.out.println("[Server] Waiting for client.");
        try(Connection conn = trans.accept( host, port, serialization ))
        {
            System.out.println( "[Server] Serving client: " + conn );
            serve( conn );
            System.out.println( "[Server] Connection lost." );
        }
    }

    private void serve( Connection conn ) throws IOException
    {
        while ( true )
        {
            long id = conn.recieve();
            conn.send( id );
        }
    }

}
