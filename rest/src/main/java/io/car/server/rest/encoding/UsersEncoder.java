/*
 * Copyright (C) 2013  Christian Autermann, Jan Alexander Wirwahn,
 *                     Arne De Wall, Dustin Demuth, Saqib Rasheed
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.car.server.rest.encoding;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.User;
import io.car.server.core.entities.Users;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UsersResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UsersEncoder extends AbstractEntityEncoder<Users> {
    @Override
    public ObjectNode encode(Users t, MediaType mediaType) {
        ObjectNode root = getJsonFactory().objectNode();
        ArrayNode users = root.putArray(JSONConstants.USERS_KEY);
        UriBuilder b = getUriInfo().getBaseUriBuilder()
                .path(RootResource.class)
                .path(RootResource.USERS)
                .path(UsersResource.USER);
        for (User u : t) {
            URI uri = b.build(u.getName());
            ObjectNode user = users.addObject();
            user.put(JSONConstants.NAME_KEY, u.getName());
            user.put(JSONConstants.HREF_KEY, uri.toString());
        }
        return root;
    }
}
