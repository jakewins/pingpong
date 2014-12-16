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
package pp.message;

import javax.xml.ws.Provider;

/**
 * Just some common statics to help the stmt/response messaging.
 */
public class StmtResponse
{
    public static class Pair<A,B>
    {
        public final A a;
        public final B b;

        public Pair( A a, B b )
        {
            this.a = a;
            this.b = b;
        }
    }

    public static class Node
    {
        public final String id;
        public final String label;
        public final Pair<String, Object>[] properties;

        public Node( String id, String label, Pair<String,Object>[] properties )
        {
            this.id = id;
            this.label = label;
            this.properties = properties;
        }
    }

    public static final String QUERY = "MATCH (asdiajsfoiargoiahelrghaelirghealrghaelgherliguhaerg) " +
                                         "FILTER auhiuashdfiuahsfiuahrflisdhfgliudsfhgiusdhfgliusdfhg " +
                                         "WHILE oahefihrgharighseriuhgiuehglusegsehglaehgiuhgluhihlai" +
                                         "MATCH (asdiajsfoiargoiahelrghaelirghealrghaelgherliguhaerg) " +
                                         "FILTER auhiuashdfiuahsfiuahrflisdhfgliudsfhgiusdhfgliusdfhg " +
                                         "WHILE oahefihrgharighseriuhgiuehglusegsehglaehgiuhgluhihlai" +
                                         "MATCH (asdiajsfoiargoiahelrghaelirghealrghaelgherliguhaerg) " +
                                         "FILTER auhiuashdfiuahsfiuahrflisdhfgliudsfhgiusdhfgliusdfhg " +
                                         "WHILE oahefihrgharighseriuhgiuehglusegsehglaehgiuhgluhihlai";

    public static final String[] PARAMS = new String[]{"param1", "param2", "param3"};

    public static final Object[][] RESULT = repeat( 50, nodes() );


    private static final Object[][] repeat( int times, Provider<Object[]> rowFactory )
    {
        Object[][] out = new Object[times][];
        for ( int i = 0; i < times; i++ )
        {
            out[i] = rowFactory.invoke( new Object[5] );
        }
        return out;
    }

    private static Provider<Object[]> nodes()
    {
        return new Provider<Object[]>()
        {
            @Override
            public Object[] invoke( Object[] request )
            {
                for ( int i = 0; i < request.length; i++ )
                {
                    request[i] = new Node("N" + i, "User", new Pair[]{
                            new Pair("name", "Bob"),
                            new Pair("age", 12),
                            new Pair("favorite_shoes", new String[]{"Shoe 1", "Shoe 2", "Shoe 3"})
                    });
                }
                return request;
            }
        };
    }

}
