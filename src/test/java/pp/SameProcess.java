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
import pp.transport.Transports;

import java.io.IOException;

public class SameProcess
{
    private static final Transports transport = Transports.OIO;
    private static final Serialization serialization = Serialization.MSGPACK_SMALL;

    public static void main(String ... args) throws InterruptedException
    {
        Thread server = new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Server.main( serialization.name(), transport.name() );
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( e );
                }
            }
        } );
        server.start();
        Thread client = new Thread( new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Client.main( serialization.name(), transport.name(), "localhost:7474", "10" );
                }
                catch ( IOException e )
                {
                    throw new RuntimeException( e );
                }
            }
        } );
        client.start();

        client.join();
        server.stop();
    }
}
