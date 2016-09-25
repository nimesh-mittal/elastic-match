/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.db;

import com.ftt.elastic.match.beans.PageInfo;
import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.utils.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author nimeshagarwal
 */
public class ARecordDAO {

    private final EntityDAO<ARecord> entityDAO;

    public ARecordDAO() {
        Datastore datastore = ConnectionFactory.getDatastore();
        datastore.ensureIndexes();
        entityDAO = new EntityDAO(datastore, ARecord.class);
    }

    public String create(ARecord record) {
        return (String) entityDAO.create(record);
    }

    public ARecord get(String id) {
        return entityDAO.getOne(id);
    }

    public void delete(String id) {
        entityDAO.delete(id);
    }

    public Integer update(String id, String fieldName, Object fieldValue) {
        Map<String, Object> search = new HashMap<>();
        search.put("_id =", id);

        Map<String, Object> update = new HashMap<>();
        update.put(fieldName, fieldValue);
        return entityDAO.update(search, update);
    }

    public Integer update(String id, Map<String, Object> updates) {
        Map<String, Object> search = new HashMap<>();
        search.put("_id =", id);

        Map<String, Object> update = new HashMap<>();
        update.putAll(updates);
        return entityDAO.update(search, update);
    }

    public List<ARecord> filter(Map<String, Object> searchCriteria, PageInfo pageInfo, String sortCriteria) {
        return entityDAO.filter(searchCriteria, sortCriteria, pageInfo);
    }

    public List<String> distinct(String matchName) {
        //TODO: use pagination and lock the keys so that other processes/threads should not pick it up
        Map<String, Object> where = new HashMap<>();
        where.put("matchMetadata.status", Constants.MatchingStatus.UnMatch.name());
        where.put("matchName", matchName);
        return entityDAO.distinct("key", where);
    }
}
