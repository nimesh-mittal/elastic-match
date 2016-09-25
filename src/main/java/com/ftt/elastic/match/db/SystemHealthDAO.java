/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.db;

import com.ftt.elastic.match.beans.SystemHealth;
import org.mongodb.morphia.Datastore;

/**
 *
 * @author nimeshagarwal
 */
public class SystemHealthDAO {
    
    private final EntityDAO<SystemHealth> entityDAO;
    
    public SystemHealthDAO() {
        Datastore datastore = ConnectionFactory.getDatastore();
        datastore.ensureIndexes();
        entityDAO = new EntityDAO(datastore, SystemHealth.class);
    }
    
    public void create(SystemHealth health) {
        health.setId("match.engine.system.health");
        entityDAO.create(health);
    }
    
    public SystemHealth get() {
        return entityDAO.get("match.engine.system.health");
    }
}
