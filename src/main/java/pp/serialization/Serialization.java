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
import pp.serialization.vanilla.VanillaDeserializer;
import pp.serialization.vanilla.VanillaSerializer;

public enum Serialization
{
    /**
     * Meant to be a baseline. This serializer does very little, its format consists of 8 bytes, representing the
     * message id.
     */
    VANILLA
    {
        @Override
        public Serializer newSerializer()
        {
            return new VanillaSerializer();
        }

        @Override
        public Deserializer newDeserializer()
        {
            return new VanillaDeserializer();
        }
    },

    MSGPACK_SMALL
    {
        @Override
        public Serializer newSerializer()
        {
            return new MsgPackSmall.Ser();
        }

        @Override
        public Deserializer newDeserializer()
        {
            return new MsgPackSmall.Des();
        }
    };

    public abstract Serializer newSerializer();
    public abstract Deserializer newDeserializer();

    @Override
    public String toString()
    {
        return name();
    }

}
