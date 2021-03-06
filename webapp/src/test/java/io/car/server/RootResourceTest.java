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
package io.car.server;

import static io.car.server.matchers.JerseyMatchers.hasProperty;
import static io.car.server.matchers.JerseyMatchers.hasStatus;
import static io.car.server.matchers.JerseyMatchers.isCompatible;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import io.car.server.rest.JSONConstants;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class RootResourceTest extends ResourceTestBase {
    @Test
    public void testGetRoot() {
        ClientResponse response = resource().path("/rest")
                .accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

        assertThat(response, hasStatus(Status.OK));
        assertThat(response.getType(), isCompatible(MediaType.APPLICATION_JSON_TYPE));

        JsonNode root = response.getEntity(JsonNode.class);
        assertThat(root, is(notNullValue()));

        assertThat(root, hasProperty(JSONConstants.GROUPS_KEY));
        assertThat(root.get(JSONConstants.GROUPS_KEY).asText(), instanceOf(String.class));

        assertThat(root, hasProperty(JSONConstants.USERS_KEY));
        assertThat(root.get(JSONConstants.USERS_KEY).asText(), instanceOf(String.class));
    }

    @Test
    public void testPostRoot() {
        assertThat(resource().path("/rest").post(ClientResponse.class)
                .getStatus(), is(405));
    }

    @Test
    public void testPutRoot() {
        assertThat(resource().path("/rest").put(ClientResponse.class)
                .getStatus(), is(405));
    }

    @Test
    public void testDeleteRoot() {
        assertThat(resource().path("/rest").delete(ClientResponse.class)
                .getStatus(), is(405));
    }
}
