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
package pp.transport.oio;

import pp.serialization.Deserializer;
import pp.serialization.Serialization;
import pp.serialization.Serializer;
import pp.transport.Transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/** Old IO transport */
public class OIOConnection implements Transport.Connection
{
    public final static int MAX_MESSAGE_SIZE = 1024 * 1024;

    private final ByteBuffer in = ByteBuffer.allocateDirect( MAX_MESSAGE_SIZE + 4 );
    private final ByteBuffer out = ByteBuffer.allocateDirect( MAX_MESSAGE_SIZE + 4 );

    private final SocketChannel ch;
    private final Serializer ser;
    private final Deserializer des;

    public static OIOConnection accept(String host, int port, Serialization sers) throws IOException
    {
        try(ServerSocketChannel serverCh = ServerSocketChannel.open())
        {
            serverCh.bind( new InetSocketAddress( host, port ) );
            serverCh.configureBlocking( true );
            return new OIOConnection(
                serverCh.accept(),
                sers.newServerSerializer(),
                sers.newServerDeserializer());
        }
    }

    public static OIOConnection connect(String host, int port, Serialization sers) throws IOException
    {
        return new OIOConnection(
                SocketChannel.open( new InetSocketAddress( host, port ) ),
                sers.newClientSerializer(),
                sers.newClientDeserializer());
    }


    public OIOConnection( SocketChannel ch, Serializer serializer, Deserializer deserializer ) throws IOException
    {
        ch.configureBlocking( true );
        this.ch = ch;
        this.ser = serializer;
        this.des = deserializer;
    }

    @Override
    public void send( long msgId ) throws IOException
    {
        // Sending a message consists of
        // - Serialize a response
        out.position( 4 ); out.limit( out.capacity() );
        int outSize = ser.serialize( msgId, out );

        out.putInt( 0, outSize );

        // - Send message
        out.position(0); out.limit( outSize + 4 ); int written = 0;
        while ( written < outSize + 4 ) { written += ch.write( out ); }
    }

    @Override
    public long recieve() throws IOException
    {
        // Reading a message consists of
        // - Read a small framing header (msg size)
        in.position( 0 ); in.limit( 4 ); int read = 0;
        while ( read < 4 ) { read += ch.read( in ); }
        int inSize = in.getInt( 0 );

        in.position( 0 ); in.limit( inSize ); read = 0;
        while ( read < inSize ) { read += ch.read( in ); }

        // - Deserialize the message, extracting a message id
        in.position( 0 );
        return des.deserialize( inSize, in );
    }

    @Override
    public void close() throws IOException
    {
        ch.close();
    }
}
