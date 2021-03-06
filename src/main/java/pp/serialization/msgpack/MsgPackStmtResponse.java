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
package pp.serialization.msgpack;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.BufferUnpacker;
import pp.infrastructure.BBMsgPacker;
import pp.message.StmtResponse;
import pp.serialization.Deserializer;
import pp.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

import static pp.message.StmtResponse.Node;
import static pp.message.StmtResponse.Pair;

public class MsgPackStmtResponse
{
    public static class Server
    {
        public static class Ser implements Serializer
        {
            private final MessagePack msgpack = new MessagePack();
            private final BBMsgPacker packer = new BBMsgPacker(msgpack);

            @Override
            public int serialize( long id, ByteBuffer outBuf ) throws IOException
            {
                packer.wrap( outBuf );
                packer.write( id );

                packer.writeArrayBegin( StmtResponse.RESULT.length );
                for ( int i = 0; i < StmtResponse.RESULT.length; i++ )
                {
                    serializeRow( packer, StmtResponse.RESULT[i] );
                }
                packer.writeArrayEnd();

                return outBuf.position();
            }

            private void serializeRow( Packer packer, Object[] row ) throws IOException
            {
                packer.writeArrayBegin( row.length );
                for ( int j = 0; j < row.length; j++ )
                {
                    Object cell = row[j];
                    if(cell instanceof Node)
                    {
                        Node n = (Node)cell;
                        packer.write(n.id);
                        packer.write(n.label);
                        packer.writeMapBegin( n.properties.length );
                        for ( int i = 0; i < n.properties.length; i++ )
                        {
                            Pair<String,Object> prop = n.properties[i];
                            packer.write( prop.a );
                            serializeCell( packer, prop.b );
                        }
                        packer.writeMapEnd();
                    }
                    else
                    {
                        throw new RuntimeException( "Unknown type: " + cell );
                    }
                }
                packer.writeArrayEnd();
            }

            private void serializeCell( Packer packer, Object val ) throws IOException
            {
                if(val instanceof String)
                {
                    packer.write( (String)val );
                }
                else if(val instanceof Integer)
                {
                    packer.write( (Integer)val );
                }
                else if(val.getClass().isArray())
                {
                    Object[] arr = (Object[])val;
                    packer.writeArrayBegin( arr.length );
                    for ( int i = 0; i < arr.length; i++ )
                    {
                        serializeCell( packer, arr[i] );
                    }
                    packer.writeArrayEnd();
                }
            }
        }

        public static class Des implements Deserializer
        {
            private final MessagePack msgpack = new MessagePack();
            private final BufferUnpacker unpacker = msgpack.createBufferUnpacker();

            @Override
            public long deserialize( int size, ByteBuffer buffer ) throws IOException
            {
                return unpacker.wrap( buffer ).readLong();
            }
        }
    }

    public static class Client
    {
        public static class Ser implements Serializer
        {
            private final MessagePack msgpack = new MessagePack();
            private final BBMsgPacker packer = new BBMsgPacker(msgpack);

            @Override
            public int serialize( long id, ByteBuffer outBuf ) throws IOException
            {
                packer.wrap( outBuf );
                packer
                    .write( id )
                    .write( StmtResponse.QUERY );

                packer.writeArrayBegin( StmtResponse.PARAMS.length );
                for ( int i = 0; i < StmtResponse.PARAMS.length; i++ )
                {
                    packer.write( StmtResponse.PARAMS[i] );
                }
                packer.writeArrayEnd();
                return outBuf.position();
            }
        }

        public static class Des implements Deserializer
        {
            private final MessagePack msgpack = new MessagePack();
            private final BufferUnpacker unpacker = msgpack.createBufferUnpacker();

            @Override
            public long deserialize( int size, ByteBuffer buffer ) throws IOException
            {
                return unpacker.wrap( buffer ).readLong();
            }
        }
    }

}
