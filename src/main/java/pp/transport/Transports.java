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
package pp.transport;

import pp.serialization.Serialization;
import pp.transport.oio.OIOConnection;

import java.io.IOException;

public enum Transports implements Transport
{
    /** Old IO, simple blocking sockets. */
    OIO
    {
        @Override
        public Connection connect( String host, int port, Serialization sers ) throws IOException
        {
            return OIOConnection.connect( host, port, sers );
        }

        @Override
        public Connection accept( String host, int port, Serialization sers ) throws IOException
        {
            return OIOConnection.accept( host, port, sers );
        }
    };

    @Override
    public String toString()
    {
        return name();
    }
}
