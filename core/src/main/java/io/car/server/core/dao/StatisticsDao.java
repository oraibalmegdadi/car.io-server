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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.car.server.core.dao;

import io.car.server.core.entities.Phenomenon;
import io.car.server.core.entities.Phenomenons;
import io.car.server.core.entities.Track;
import io.car.server.core.entities.User;
import io.car.server.core.statistics.Statistic;
import io.car.server.core.statistics.Statistics;

/**
 *
 * @author jan
 */
public interface StatisticsDao {
    Statistics getStatisticsForTrack(Track track);

    Statistics getStatisticsForUser(User user);

    Statistics getStatistics();

    Statistic getStatisticsForTrack(Track track, Phenomenon phenomenon);

    Statistic getStatisticsForUser(User user, Phenomenon phenomenon);

    Statistic getStatistics(Phenomenon phenomenon);

    Statistics getStatisticsForTrack(Track track, Phenomenons phenomenons);

    Statistics getStatisticsForUser(User user, Phenomenons phenomenons);

    Statistics getStatistics(Phenomenons phenomenons);
}
