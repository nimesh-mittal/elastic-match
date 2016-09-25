/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.db;

import com.ftt.elastic.match.utils.PropertiesRepo;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 *
 * @author nimeshagarwal
 */
public class ConnectionFactory {

    private static Datastore datastore;

    public static synchronized  Datastore getDatastore() {
        if (datastore == null) {
            Morphia morphia = new Morphia();
            morphia.mapPackage("com.ftt.elastic.match.beans");
            MongoClient mongoClient = new MongoClient(PropertiesRepo.get("db.hostname"), PropertiesRepo.getInt("db.port"));
            datastore = morphia.createDatastore(mongoClient, "match");
        }
        return datastore;
    }

}
