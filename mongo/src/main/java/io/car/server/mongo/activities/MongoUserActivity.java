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
import io.car.server.core.activities.UserActivity;
import io.car.server.core.entities.User;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoUser;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoUserActivity extends MongoActivity implements UserActivity {
    public static final String OTHER = "other";
    @Property(OTHER)
    private Key<MongoUser> other;
    @Transient
    private MongoUser _other;

    @AssistedInject
    public MongoUserActivity(MongoDB mongoDB,
                             @Assisted ActivityType type,
                             @Assisted("user") User user,
                             @Assisted("other") User other) {
        super(mongoDB, user, type);
        this._other = (MongoUser) other;
        this.other = mongoDB.reference(this._other);
    }

    @Inject
    public MongoUserActivity(MongoDB mongoDB) {
        this(mongoDB, null, null, null);
    }

    @Override
    public MongoUser getOther() {
        if (this._other == null) {
            this._other = getMongoDB().dereference(MongoUser.class, this.other);
        }
        return this._other;
    }

    @Override
    public void setOther(User user) {
        this._other = (MongoUser) user;
        this.other = getMongoDB().reference(this._other);
    }

    @Override
    public boolean hasOther() {
        return getOther() != null;
    }
}
