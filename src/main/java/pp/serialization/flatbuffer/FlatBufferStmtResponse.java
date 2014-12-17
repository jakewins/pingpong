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
package pp.serialization.flatbuffer;

import com.google.flatbuffers.FlatBufferBuilder;
import pp.flatbuf.NCollection;
import pp.flatbuf.Statement;
import pp.message.StmtResponse;
import pp.serialization.Deserializer;
import pp.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class FlatBufferStmtResponse
{
    public static class Server
    {
        public static class Ser implements Serializer
        {
            @Override
            public int serialize( long id, ByteBuffer outBuf ) throws IOException
            {
                return 0;
            }
        }

        public static class Des implements Deserializer
        {
            @Override
            public long deserialize( int size, ByteBuffer buffer ) throws IOException
            {
                return 0;
            }
        }
    }

    public static class Client
    {
        public static class Ser implements Serializer
        {
            @Override
            public int serialize( long id, ByteBuffer outBuf ) throws IOException
            {
                FlatBufferBuilder fbb = new FlatBufferBuilder(outBuf);

                int query = fbb.createString( StmtResponse.QUERY );
                Statement.startStatement( fbb );
                Statement.addQuery( fbb, query );
                NCollection.
                Statement.addParamsType( fbb, NCollection.ty );

                return 0;
            }
        }

        public static class Des implements Deserializer
        {
            @Override
            public long deserialize( int size, ByteBuffer buffer ) throws IOException
            {
                return 0;
            }
        }
    }
}
