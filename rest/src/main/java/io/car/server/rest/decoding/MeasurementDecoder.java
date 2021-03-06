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
package io.car.server.rest.decoding;

import java.util.Iterator;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Geometry;

import io.car.server.core.dao.PhenomenonDao;
import io.car.server.core.dao.SensorDao;
import io.car.server.core.entities.Measurement;
import io.car.server.core.entities.MeasurementValue;
import io.car.server.core.entities.Phenomenon;
import io.car.server.core.util.GeoJSONConstants;
import io.car.server.rest.JSONConstants;

/**
 * @author Arne de Wall <a.dewall@52north.org>
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public class MeasurementDecoder extends AbstractEntityDecoder<Measurement> {
    private final EntityDecoder<Geometry> geometryDecoder;
    private final PhenomenonDao phenomenonDao;
    private final SensorDao sensorDao;

    @Inject
    public MeasurementDecoder(EntityDecoder<Geometry> geometryDecoder,
                              PhenomenonDao phenomenonDao,
                              SensorDao sensorDao) {
        this.geometryDecoder = geometryDecoder;
        this.phenomenonDao = phenomenonDao;
        this.sensorDao = sensorDao;
    }

    @Override
    public Measurement decode(JsonNode j, MediaType mediaType) {
        Measurement measurement = getEntityFactory().createMeasurement();
        if (j.has(JSONConstants.GEOMETRY_KEY)) {
            measurement.setGeometry(geometryDecoder.decode(j
                    .path(JSONConstants.GEOMETRY_KEY), mediaType));
        }
        if (j.has(GeoJSONConstants.PROPERTIES_KEY)) {
            JsonNode p = j.path(GeoJSONConstants.PROPERTIES_KEY);
            if (p.has(JSONConstants.SENSOR_KEY)) {
                measurement.setSensor(sensorDao.getByName(p
                        .path(JSONConstants.SENSOR_KEY)
                        .path(JSONConstants.NAME_KEY).textValue()));
            }
            if (p.has(JSONConstants.TIME_KEY)) {
                measurement.setTime(getDateTimeFormat().parseDateTime(p
                        .path(JSONConstants.TIME_KEY).textValue()));
            }

            if (p.has(JSONConstants.PHENOMENONS_KEY)) {
                JsonNode phens = p.path(JSONConstants.PHENOMENONS_KEY);
                Iterator<Entry<String, JsonNode>> fields = phens.fields();
                while (fields.hasNext()) {
                    Entry<String, JsonNode> field = fields.next();
                    Phenomenon phenomenon = phenomenonDao.getByName(field
                            .getKey());
                    JsonNode valueNode = field.getValue()
                            .get(JSONConstants.VALUE_KEY);
                    if (valueNode.isValueNode()) {
                        Object value = null;
                        if (valueNode.isNumber()) {
                            value = valueNode.asDouble();
                        } else if (valueNode.isBoolean()) {
                            value = valueNode.booleanValue();
                        } else if (valueNode.isTextual()) {
                            value = valueNode.textValue();
                        }
                        MeasurementValue v = getEntityFactory()
                                .createMeasurementValue();
                        v.setValue(value);
                        v.setPhenomenon(phenomenon);
                        measurement.addValue(v);
                    }
                }
            }
        }
        return measurement;
    }
}
