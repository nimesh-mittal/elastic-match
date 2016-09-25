package com.ftt.elastic.match.db;

import com.ftt.elastic.match.beans.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

public class EntityDAO<T> {

    private final Datastore datastore;
    private final Class<T> genericType;

    public EntityDAO(Datastore datastore, Class<T> type) {
        this.datastore = datastore;
        this.genericType = type;
    }

    public Object create(T entity) {
        Key<T> result = datastore.save(entity);
        return result.getId();
    }

    public List<Object> create(List<T> entities) {
        Iterable<Key<T>> result = datastore.save(entities);
        Iterator<Key<T>> iterator = result.iterator();
        List<Object> results = new ArrayList<>();
        while (iterator.hasNext()) {
            if (iterator.next() != null) {
                results.add(iterator.next().getId());
            }
        }
        return results;
    }

    public List<String> distinct(String fieldName, Map<String, Object> whereConditions) {
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.putAll(whereConditions);
        DBCollection dBCollection = datastore.getCollection(genericType);
        return dBCollection.distinct(fieldName, dbObject);
    }

    /**
     *
     * @param searchCriteria key is string containing field name and operator
     * and value is RHS operand i.e., "Location in" => "Delhi"
     * @param updateFieldValue key is field name and value is new value needs to
     * set
     * @return
     */
    public Integer update(Map<String, Object> searchCriteria, Map<String, Object> updateFieldValue) {
        Query<T> updateQuery = datastore.createQuery(genericType);

        for (Map.Entry<String, Object> entry : searchCriteria.entrySet()) {
            updateQuery.filter(entry.getKey(), entry.getValue());
        }

        UpdateOperations<T> updateOperations = datastore.createUpdateOperations(genericType);
        for (Map.Entry<String, Object> entry : updateFieldValue.entrySet()) {
            updateOperations.set(entry.getKey(), entry.getValue());
        }

        UpdateResults results = datastore.update(updateQuery, updateOperations);
        return results.getUpdatedCount();
    }

    public void delete(String id) {
        Query<T> query = datastore.createQuery(genericType);
        query.field("_id").equal(id);
        datastore.delete(query);
    }

    public void delete(List<String> ids) {
        Query<T> query = datastore.createQuery(genericType);
        query.field("_id").in(ids);
        datastore.delete(query);
    }

    /**
     *
     * @param queryCriteria - key is field and operator and value is RHS value
     * eg: "Location in" => "Delhi"
     * @param sortCriteria - comma separated field names. use - for descending
     * order eg: "location, -status"
     * @param pageInfo - contain limit and offset
     * @return
     */
    public List<T> filter(Map<String, Object> queryCriteria, String sortCriteria, PageInfo pageInfo) {
        Query<T> query = datastore.createQuery(genericType);

        for (Map.Entry<String, Object> entry : queryCriteria.entrySet()) {
            query = query.filter(entry.getKey(), entry.getValue());
        }

        if (pageInfo == null) {
            pageInfo = new PageInfo(100000, 0);
        }

        if (sortCriteria == null) {
            sortCriteria = "_id";
        }

        query = query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset());
        query = query.order(sortCriteria);

        return query.asList();
    }

    public T getOne(String id) {
        ObjectId objectId = new ObjectId(id);
        return datastore.get(genericType, objectId);
    }

    public T get(String id) {
        return datastore.get(genericType, id);
    }

    /**
     * @param ids
     * @param sortCriteria - comma separated field names. use - for descending
     * order eg: "location, -status"
     * @param pageInfo - contain limit and offset
     * @return
     */
    public List<T> getMany(List<String> ids, String sortCriteria, PageInfo pageInfo) {
        Query<T> query = datastore.createQuery(genericType);
        query = query.field("_id").in(ids);
        query = query.limit(pageInfo.getLimit()).offset(pageInfo.getOffset());
        query = query.order(sortCriteria);

        return query.asList();
    }

    public Datastore getDatastore() {
        return datastore;
    }

}
