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

import org.joda.time.DateTime;

import com.github.jmkgreen.morphia.Datastore;
import com.github.jmkgreen.morphia.Key;
import com.github.jmkgreen.morphia.dao.BasicDAO;
import com.github.jmkgreen.morphia.mapping.Mapper;
import com.github.jmkgreen.morphia.query.Query;
import com.github.jmkgreen.morphia.query.UpdateOperations;
import com.github.jmkgreen.morphia.query.UpdateResults;
import com.mongodb.WriteResult;

import io.car.server.core.util.PaginatedIterable;
import io.car.server.core.util.Pagination;
import io.car.server.mongo.MongoDB;
import io.car.server.mongo.entity.MongoEntityBase;

/**
 * @param <K> the key type
 * @param <E> the entitiy type
 * @param <C> the collection type
 *
 * @author Christian Autermann <autermann@uni-muenster.de>
 */
public abstract class AbstractMongoDao<K, E, C extends PaginatedIterable<? super E>> {
    private final BasicDAO<E, K> dao;
    private MongoDB mongoDB;

    public AbstractMongoDao(Class<E> type, MongoDB mongoDB) {
        this.mongoDB = mongoDB;
        this.dao = new BasicDAO<E, K>(type, this.mongoDB.getDatastore());
    }

    protected Query<E> q() {
        return dao.createQuery();
    }

    protected UpdateOperations<E> up() {
        return dao.createUpdateOperations();
    }

    protected E get(K key) {
        return dao.get(key);
    }

    protected long count() {
        return dao.count();
    }

    protected long count(Query<E> q) {
        return dao.count(q);
    }

    protected Key<E> save(E entity) {
        return dao.save(entity);
    }

    protected UpdateResults<E> update(K key, UpdateOperations<E> ops) {
        return dao.update(q().field(Mapper.ID_KEY).equal(key), ops);
    }

    protected UpdateResults<E> update(Query<E> q, UpdateOperations<E> ops) {
        return dao.update(q, ops);
    }

    protected Iterable<E> fetch(Query<E> q) {
        return dao.find(q).fetch();
    }

    protected C fetch(Query<E> q, Pagination p) {
        long count = 0;
        if (p != null) {
            count = count(q);
            q.offset(p.getOffset()).limit(p.getLimit());
        }
        return createPaginatedIterable(fetch(q), p, count);
    }

    protected abstract C createPaginatedIterable(
            Iterable<E> i, Pagination p, long count);

    protected WriteResult delete(K id) {
        return dao.deleteById(id);
    }

    @SuppressWarnings("unchecked")
    protected void updateTimestamp(E e) {
        update((K) this.mongoDB.getMapper().getId(e), up()
                .set(MongoEntityBase.LAST_MODIFIED, new DateTime()));
    }

    public <T> T dereference(Class<T> c, Key<T> key) {
        return mongoDB.dereference(c, key);
    }

    public <T> Iterable<T> dereference(Class<T> c, Iterable<Key<T>> keys) {
        return mongoDB.dereference(c, keys);
    }

    public <T> Key<T> reference(T entity) {
        return mongoDB.reference(entity);
    }

    public Datastore getDatastore() {
        return mongoDB.getDatastore();
    }

    public Mapper getMapper() {
        return mongoDB.getMapper();
    }
}
