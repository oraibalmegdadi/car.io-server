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
package io.car.server.rest.resources;

import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import com.google.inject.Inject;
import com.google.inject.Provider;

import io.car.server.core.Service;
import io.car.server.core.entities.EntityFactory;
import io.car.server.core.entities.User;
import io.car.server.core.exception.UserNotFoundException;
import io.car.server.rest.auth.AuthConstants;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractResource {
    private Provider<SecurityContext> securityContext;
    private Provider<Service> service;
    private Provider<UriInfo> uriInfo;
    private Provider<ResourceFactory> resourceFactory;
    private Provider<EntityFactory> entityFactory;

    protected boolean canModifyUser(User user) {
        return canModifyUser(user.getName());
    }

    protected boolean canModifyUser(String user) {
        return getSecurityContext().isUserInRole(AuthConstants.ADMIN_ROLE) ||
               (getSecurityContext().getUserPrincipal() != null &&
                getSecurityContext().getUserPrincipal().getName().equals(user));
    }


    public SecurityContext getSecurityContext() {
        return securityContext.get();
    }

    public UriInfo getUriInfo() {
        return uriInfo.get();
    }

    public Service getService() {
        return service.get();
    }

    public ResourceFactory getResourceFactory() {
        return resourceFactory.get();
    }

    protected String getCurrentUser() throws UserNotFoundException {
        return getSecurityContext().getUserPrincipal().getName();
    }

    @Inject
    public void setSecurityContext(Provider<SecurityContext> securityContext) {
        this.securityContext = securityContext;
    }

    @Inject
    public void setUriInfo(Provider<UriInfo> uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Inject
    public void setUserService(Provider<Service> service) {
        this.service = service;
    }

    @Inject
    public void setResourceFactory(Provider<ResourceFactory> resourceFactory) {
        this.resourceFactory = resourceFactory;
    }

    public EntityFactory getEntityFactory() {
        return entityFactory.get();
    }

    @Inject
    public void setEntityFactory(Provider<EntityFactory> entityFactory) {
        this.entityFactory = entityFactory;
    }
}
