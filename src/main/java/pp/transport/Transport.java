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

import java.io.IOException;

public interface Transport
{
    interface Connection extends AutoCloseable
    {
        void send( long msgId ) throws IOException;
        long recieve() throws IOException;

        @Override void close() throws IOException;
    }

    /** Connect to the specified remote port */
    public Connection connect(String host, int port, Serialization sers) throws IOException;

    /** Sugar. Bind to the specified port, accept one connection and then close the public port. */
    public Connection accept( String host, int port, Serialization sers ) throws IOException;
}
