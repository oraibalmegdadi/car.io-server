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
package io.car.server.mongo.dao;

import com.google.inject.Inject;

import io.car.server.core.dao.SensorDao;
import io.car.server.core.entities.Sensor;
import io.car.server.core.entities.Sensors;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoSensor;

/**
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MongoSensorDao extends AbstractMongoDao<String, MongoSensor, Sensors>
        implements SensorDao {
    @Inject
    public MongoSensorDao(MongoDB mongoDB) {
        super(MongoSensor.class, mongoDB);
    }

    @Override
    public Sensor getByName(final String name) {
        return q().field(MongoSensor.NAME).equal(name).get();
    }

    @Override
    public Sensors get(Pagination p) {
        return fetch(q(), p);
    }

    @Override
    public Sensor create(Sensor sensor) {
        MongoSensor ms = (MongoSensor) sensor;
        save(ms);
        return ms;
    }

    @Override
    protected Sensors createPaginatedIterable(Iterable<MongoSensor> i,
                                              Pagination p, long count) {
        return Sensors.from(i).withElements(count).withPagination(p).build();
    }
}
