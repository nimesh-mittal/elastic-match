/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.db;

import com.ftt.elastic.match.beans.PageInfo;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.utils.Constants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author nimeshagarwal
 */
public class BRecordDAO {

    private final EntityDAO<BRecord> entityDAO;

    public BRecordDAO() {
        Datastore datastore = ConnectionFactory.getDatastore();
        datastore.ensureIndexes();
        entityDAO = new EntityDAO(datastore, BRecord.class);
    }

    public String create(BRecord record) {
        return (String) entityDAO.create(record);
    }

    public BRecord get(String id) {
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

    public List<BRecord> filter(Map<String, Object> searchCriteria, PageInfo pageInfo, String sortCriteria) {
        return entityDAO.filter(searchCriteria, sortCriteria, pageInfo);
    }

    public List<String> distinct() {
        Map<String, Object> where = new HashMap<>();
        where.put("matchMetadata.status", Constants.MatchingStatus.UnMatch.name());
        return entityDAO.distinct("key", where);
    }
}
