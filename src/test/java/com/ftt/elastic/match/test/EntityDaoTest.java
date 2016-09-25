package com.ftt.elastic.match.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.db.EntityDAO;
import com.mongodb.MongoClient;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import java.io.IOException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author nimeshagarwal
 */
public class EntityDaoTest {

    public EntityDaoTest() {
    }

    private static final String DATABASE_NAME = "test";
    private MongodProcess mongod;
    private MongodExecutable mongodExecutable;
    private final int port = 12345;
    private final String host = "localhost";

    @BeforeMethod
    public void beforeEach() throws Exception {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net(port, Network.localhostIsIPv6()))
                .build();
        mongodExecutable = starter.prepare(mongodConfig);
        mongod = mongodExecutable.start();
    }

    @AfterMethod
    public void afterEach() throws Exception {
        if (this.mongod != null) {
            this.mongod.stop();
            this.mongodExecutable.stop();
        }
    }

    //@Test
    public void shouldCreateNewObjectInEmbeddedMongoDb() throws IOException {
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.ftt.elastic.match.beans");
        MongoClient mongoClient = new MongoClient(host, port);
        Datastore datastore = morphia.createDatastore(mongoClient, DATABASE_NAME);

        EntityDAO<ARecord> entityDAO = new EntityDAO(datastore, ARecord.class);
        ARecord record = new ARecord();
        record.setDisplayId("myrecord");
        String id = (String) entityDAO.create(record);

        ARecord recordActual = entityDAO.getOne(id);
        Assert.assertEquals(recordActual.getDisplayId(), record.getDisplayId());
    }
}
