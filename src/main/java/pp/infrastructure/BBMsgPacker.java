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
package pp.infrastructure;

import org.msgpack.MessagePack;
import org.msgpack.io.Output;
import org.msgpack.packer.MessagePackPacker;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class BBMsgPacker extends MessagePackPacker
{
    private final ReusableByteBufferOutput bbOut;

    public BBMsgPacker( MessagePack msgpack )
    {
        super( msgpack, new ReusableByteBufferOutput() );
        bbOut = (ReusableByteBufferOutput)out;
    }

    public MessagePackPacker wrap( ByteBuffer buf )
    {
        bbOut.wrap(buf);
        return this;
    }
}

class ReusableByteBufferOutput implements Output
{
    public static interface ExpandBufferCallback
    {
        ByteBuffer call( ByteBuffer buffer, int len ) throws IOException;
    }

    private ByteBuffer buffer;
    private ExpandBufferCallback callback;

    private void reserve( int len ) throws IOException
    {
        if ( len <= buffer.remaining() )
        {
            return;
        }
        if ( callback == null )
        {
            throw new BufferOverflowException();
        }
        buffer = callback.call( buffer, len );
    }

    public void wrap( ByteBuffer bb )
    {
        buffer = bb;
    }

    @Override
    public void write( byte[] b, int off, int len ) throws IOException
    {
        reserve( len );
        buffer.put( b, off, len );
    }

    @Override
    public void write( ByteBuffer bb ) throws IOException
    {
        reserve( bb.remaining() );
        buffer.put( bb );
    }

    @Override
    public void writeByte( byte v ) throws IOException
    {
        reserve( 1 );
        buffer.put( v );
    }

    @Override
    public void writeShort( short v ) throws IOException
    {
        reserve( 2 );
        buffer.putShort( v );
    }

    @Override
    public void writeInt( int v ) throws IOException
    {
        reserve( 4 );
        buffer.putInt( v );
    }

    @Override
    public void writeLong( long v ) throws IOException
    {
        reserve( 8 );
        buffer.putLong( v );
    }

    @Override
    public void writeFloat( float v ) throws IOException
    {
        reserve( 4 );
        buffer.putFloat( v );
    }

    @Override
    public void writeDouble( double v ) throws IOException
    {
        reserve( 8 );
        buffer.putDouble( v );
    }

    @Override
    public void writeByteAndByte( byte b, byte v ) throws IOException
    {
        reserve( 2 );
        buffer.put( b );
        buffer.put( v );
    }

    @Override
    public void writeByteAndShort( byte b, short v ) throws IOException
    {
        reserve( 3 );
        buffer.put( b );
        buffer.putShort( v );
    }

    @Override
    public void writeByteAndInt( byte b, int v ) throws IOException
    {
        reserve( 5 );
        buffer.put( b );
        buffer.putInt( v );
    }

    @Override
    public void writeByteAndLong( byte b, long v ) throws IOException
    {
        reserve( 9 );
        buffer.put( b );
        buffer.putLong( v );
    }

    @Override
    public void writeByteAndFloat( byte b, float v ) throws IOException
    {
        reserve( 5 );
        buffer.put( b );
        buffer.putFloat( v );
    }

    @Override
    public void writeByteAndDouble( byte b, double v ) throws IOException
    {
        reserve( 9 );
        buffer.put( b );
        buffer.putDouble( v );
    }

    @Override
    public void flush() throws IOException
    {
    }

    @Override
    public void close()
    {
    }
}
