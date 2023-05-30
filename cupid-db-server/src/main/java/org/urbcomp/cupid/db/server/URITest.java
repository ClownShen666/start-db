/* 
 * Copyright (C) 2022  ST-Lab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.urbcomp.cupid.db.server;

import java.net.URI;

public class URITest {

    public static void main(String[] args) {
        URI uri = null;
        String host = args[0];
        System.out.println("host = " + host);
        try {
            // create a URI
            uri = new URI(null, null, host, 8848, null, null, null);

            // get the Authority
            String authority = uri.getAuthority();

            // get the Raw Authority
            String Raw_authority = uri.getRawAuthority();

            // display the URI
            System.out.println("URI = " + uri);

            // display the Authority
            System.out.println("Authority = " + authority);

            // display the Raw Authority
            System.out.println("Raw Authority = " + Raw_authority);
        }

        // if any error occurs
        catch (Exception e) {

            // display the error
            System.out.println(e);
        }
    }
}
