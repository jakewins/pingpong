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
package pp.serialization;

import pp.serialization.msgpack.MsgPackSmall;
import pp.serialization.msgpack.MsgPackStmtResponse;
import pp.serialization.sbe.SBESmall;
import pp.serialization.vanilla.Vanilla;

public enum Serialization
{
    /**
     * Meant to be a baseline. This serializer does very little, its format consists of 8 bytes, representing the
     * message id.
     */
    VANILLA
    {
        @Override public Serializer newClientSerializer()
        {
            return new Vanilla.Ser();
        }
        @Override public Deserializer newClientDeserializer()
        {
            return new Vanilla.Des();
        }
        @Override public Serializer newServerSerializer()
        {
            return new Vanilla.Ser();
        }
        @Override public Deserializer newServerDeserializer()
        {
            return new Vanilla.Des();
        }
    },

    MSGPACK_SMALL
    {
        @Override public Serializer newClientSerializer() { return new MsgPackSmall.Ser(); }
        @Override public Deserializer newClientDeserializer() { return new MsgPackSmall.Des(); }
        @Override public Serializer newServerSerializer() { return new MsgPackSmall.Ser(); }
        @Override public Deserializer newServerDeserializer() { return new MsgPackSmall.Des(); }
    },

    MSGPACK_STMT_RESPONSE
    {
        @Override public Serializer newClientSerializer() { return new MsgPackStmtResponse.Client.Ser(); }
        @Override public Deserializer newClientDeserializer() { return new MsgPackStmtResponse.Client.Des(); }
        @Override public Serializer newServerSerializer() { return new MsgPackStmtResponse.Server.Ser(); }
        @Override public Deserializer newServerDeserializer() { return new MsgPackStmtResponse.Server.Des(); }
    },

    SBE_SMALL
    {
        @Override public Serializer newClientSerializer() { return new SBESmall.Ser(); }
        @Override public Deserializer newClientDeserializer() { return new SBESmall.Des(); }
        @Override public Serializer newServerSerializer() { return new SBESmall.Ser(); }
        @Override public Deserializer newServerDeserializer() { return new SBESmall.Des(); }
    },


    // Was not able to implement this in a sensible way with SBE
//    SBE_STMT_RESPONSE
//    {
//        @Override public Serializer newClientSerializer() { return new SBEStmtResponse.Client.Ser(); }
//        @Override public Deserializer newClientDeserializer() { return new SBEStmtResponse.Client.Des(); }
//        @Override public Serializer newServerSerializer() { return new SBEStmtResponse.Server.Ser(); }
//        @Override public Deserializer newServerDeserializer() { return new SBEStmtResponse.Server.Des(); }
//    }

    ;

    public abstract Serializer newClientSerializer();
    public abstract Serializer newServerSerializer();
    public abstract Deserializer newClientDeserializer();
    public abstract Deserializer newServerDeserializer();

    @Override
    public String toString()
    {
        return name();
    }

}
