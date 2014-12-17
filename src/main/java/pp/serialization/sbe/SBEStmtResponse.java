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
package pp.serialization.sbe;

import baseline.SimpleMessage;
import baseline.Statement;
import pp.message.StmtResponse;
import pp.serialization.Deserializer;
import pp.serialization.Serializer;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class SBEStmtResponse
{
    // NOTE: This is not completed. I could not find a sensible way, that didn't include including serialized things
    // inside serialized SBE messages to handle complex structures like recursive maps.
    public static class Server
    {
        public static class Ser implements Serializer
        {
            private final DirectBuffer directBuffer = new DirectBuffer( new byte[0] );
            private final SimpleMessage SIMPLE_MESSAGE = new SimpleMessage();

            @Override
            public int serialize( long id, ByteBuffer outBuf ) throws IOException
            {
                directBuffer.wrap( outBuf );
                SIMPLE_MESSAGE.wrapForEncode( directBuffer, 0 ).id( id );
                return SIMPLE_MESSAGE.size();
            }
        }

        public static class Des implements Deserializer
        {
            private final DirectBuffer directBuffer = new DirectBuffer( new byte[0] );
            private final Statement stmt = new Statement();
            private final int blockLength = stmt.sbeBlockLength();
            private final int schemaVersion = stmt.sbeSchemaVersion();

            @Override
            public long deserialize( int size, ByteBuffer buffer ) throws IOException
            {
                directBuffer.wrap( buffer );
                return stmt.wrapForDecode( directBuffer, 0, blockLength, schemaVersion ).id();
            }
        }
    }

    public static class Client
    {
        public static class Ser implements Serializer
        {
            private final DirectBuffer directBuffer = new DirectBuffer( new byte[0] );
            private final Statement stmt = new Statement();

            @Override
            public int serialize( long id, ByteBuffer outBuf ) throws IOException
            {
                if(true) throw new UnsupportedOperationException( "Not yet implemented." );
                final int srcOffset = 0;

                directBuffer.wrap( outBuf );

                stmt.wrapForEncode( directBuffer, 0 )
                        .id( id );

                byte[] query = StmtResponse.QUERY.getBytes( StandardCharsets.UTF_8 );
                stmt.putQuery( query, srcOffset, query.length );
                return stmt.size();
            }
        }

        public static class Des implements Deserializer
        {
            private final DirectBuffer directBuffer = new DirectBuffer( new byte[0] );
            private final SimpleMessage simpleMessage = new SimpleMessage();
            private final int blockLength = simpleMessage.sbeBlockLength();
            private final int schemaVersion = simpleMessage.sbeSchemaVersion();

            @Override
            public long deserialize( int size, ByteBuffer buffer ) throws IOException
            {
                directBuffer.wrap( buffer );
                return simpleMessage.wrapForDecode( directBuffer, 0, blockLength, schemaVersion ).id();
            }
        }
    }
}
