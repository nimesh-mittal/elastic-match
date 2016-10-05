/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.db;

import com.ftt.elastic.match.utils.Constants;
import com.ftt.elastic.match.utils.PropertiesRepo;
import com.mongodb.MongoClient;
import java.util.Objects;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 *
 * @author nimeshagarwal
 */
public class ConnectionFactory {

    private static Datastore datastore = null;

    public static synchronized  Datastore getDatastore() {
        if (Objects.isNull(datastore)) {
            Morphia morphia = new Morphia();
            morphia.mapPackage("com.ftt.elastic.match.beans");
            MongoClient mongoClient = new MongoClient(PropertiesRepo.get(Constants.Settings.MONGODB_HOST), PropertiesRepo.getInt(Constants.Settings.MONGODB_PORT));
            datastore = morphia.createDatastore(mongoClient, PropertiesRepo.get(Constants.Settings.MONGODB_SCHEMANAME));
        }
        return datastore;
    }

}
