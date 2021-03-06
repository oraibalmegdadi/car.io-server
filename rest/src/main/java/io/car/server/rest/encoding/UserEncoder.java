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

import com.fasterxml.jackson.databind.node.ObjectNode;

import io.car.server.core.entities.User;
import io.car.server.rest.JSONConstants;
import io.car.server.rest.MediaTypes;
import io.car.server.rest.resources.RootResource;
import io.car.server.rest.resources.UserResource;
import io.car.server.rest.resources.UsersResource;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class UserEncoder extends AbstractEntityEncoder<User> {

    @Override
    public ObjectNode encode(User t, MediaType mediaType) {
        ObjectNode j = getJsonFactory().objectNode();
        if (t.hasName()) {
            j.put(JSONConstants.NAME_KEY, t.getName());
        }
        if (mediaType.equals(MediaTypes.USER_TYPE)) {
            URI measurements = getUriInfo().getAbsolutePathBuilder()
                    .path(UserResource.MEASUREMENTS).build();
            URI groups = getUriInfo().getAbsolutePathBuilder()
                    .path(UserResource.GROUPS).build();
            URI friends = getUriInfo().getAbsolutePathBuilder()
                    .path(UserResource.FRIENDS).build();
            URI tracks = getUriInfo().getAbsolutePathBuilder()
                    .path(UserResource.TRACKS).build();
            URI statistics = getUriInfo().getAbsolutePathBuilder()
                    .path(UserResource.STATISTICS).build();
            URI activities = getUriInfo().getAbsolutePathBuilder()
                    .path(UserResource.ACTIVITIES).build();
            if (t.hasMail()) {
                j.put(JSONConstants.MAIL_KEY, t.getMail());
            }
            if (t.hasCreationTime()) {
                j.put(JSONConstants.CREATED_KEY,
                      getDateTimeFormat().print(t.getCreationTime()));
            }
            if (t.hasModificationTime()) {
                j.put(JSONConstants.MODIFIED_KEY,
                      getDateTimeFormat().print(t.getModificationTime()));
            }
            j.put(JSONConstants.MEASUREMENTS_KEY, measurements.toString());
            j.put(JSONConstants.GROUPS_KEY, groups.toString());
            j.put(JSONConstants.FRIENDS_KEY, friends.toString());
            j.put(JSONConstants.TRACKS_KEY, tracks.toString());
            j.put(JSONConstants.STATISTICS_KEY, statistics.toString());
            j.put(JSONConstants.ACTIVITIES_KEY, activities.toString());
        } else {
            URI uri = getUriInfo().getBaseUriBuilder()
                    .path(RootResource.class)
                    .path(RootResource.USERS)
                    .path(UsersResource.USER).build(t.getName());
            return j.put(JSONConstants.HREF_KEY, uri.toString());

        }
        return j;
    }
}
