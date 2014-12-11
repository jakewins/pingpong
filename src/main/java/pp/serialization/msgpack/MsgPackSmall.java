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
import org.msgpack.annotation.Message;
import pp.serialization.Deserializer;
import pp.serialization.Serializer;

import java.io.IOException;
import java.nio.ByteBuffer;

public class MsgPackSmall
{
    @Message
    public static class SmallMessage
    {
        public long id;

        public SmallMessage(){}
        public SmallMessage( long id )
        {
            this.id = id;
        }
    }

    public static class Ser implements Serializer
    {
        private final MessagePack msgpack = new MessagePack();

        @Override
        public int serialize( long id, ByteBuffer outBuf ) throws IOException
        {
            byte[] write = msgpack.write( new SmallMessage(id) );
            outBuf.put( write );
            return write.length;
        }
    }

    public static class Des implements Deserializer
    {
        private final MessagePack msgpack = new MessagePack();

        @Override
        public long deserialize( int size, ByteBuffer buffer ) throws IOException
        {
            SmallMessage msg = msgpack.read( buffer, SmallMessage.class );
            return msg.id;
        }
    }
}
