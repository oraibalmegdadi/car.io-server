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
package io.car.server.mongo.activities;

import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.annotations.Property;
import com.github.jmkgreen.morphia.annotations.Transient;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import io.car.server.core.activities.ActivityType;
import io.car.server.core.activities.GroupActivity;
import io.car.server.core.entities.Group;
import io.car.server.core.entities.User;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoGroup;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoGroupActivity extends MongoActivity implements GroupActivity {
    public static final String GROUP = "group";
    @Property(GROUP)
    private Key<MongoGroup> group;
    @Transient
    private MongoGroup _group;

    @AssistedInject
    public MongoGroupActivity(MongoDB mongoDB,
                              @Assisted ActivityType type,
                              @Assisted User user,
                              @Assisted Group group) {
        super(mongoDB, user, type);
        this._group = (MongoGroup) group;
        this.group = mongoDB.reference(this._group);
    }

    @Inject
    public MongoGroupActivity(MongoDB mongoDB) {
        this(mongoDB, null, null, null);
    }


    @Override
    public MongoGroup getGroup() {
        if (this._group == null) {
            this._group = getMongoDB().dereference(MongoGroup.class, this.group);
        }
        return this._group;
    }

    @Override
    public void setGroup(Group group) {
        this._group = (MongoGroup) group;
        this.group = getMongoDB().reference(this._group);
    }

    @Override
    public boolean hasGroup() {
        return getGroup() != null;
    }
}
