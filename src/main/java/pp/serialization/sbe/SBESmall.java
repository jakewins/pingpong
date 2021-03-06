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
import pp.serialization.Deserializer;
import pp.serialization.Serializer;
import uk.co.real_logic.sbe.codec.java.DirectBuffer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class SBESmall
{
    public static class Ser implements Serializer
    {
        private final DirectBuffer directBuffer = new DirectBuffer(new byte[0]);
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
        private final DirectBuffer directBuffer = new DirectBuffer(new byte[0]);
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
