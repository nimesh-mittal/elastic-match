/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.db;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.beans.Report;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.aggregation.Accumulator;
import static org.mongodb.morphia.aggregation.Group.grouping;
import static org.mongodb.morphia.aggregation.Group.id;

/**
 *
 * @author nimeshagarwal
 */
public class ReportDAO {

    private final EntityDAO<Report> entityDAO;

    public ReportDAO() {
        Datastore datastore = ConnectionFactory.getDatastore();
        datastore.ensureIndexes();
        entityDAO = new EntityDAO(datastore, Report.class);
    }

    private <H, U> Iterator<U> report(Class<H> inputType, Class<U> outputType) {
        return entityDAO.getDatastore().createAggregation(inputType)
                .group(id(grouping("matchName"), 
                        grouping("matchMetadata.status"), 
                        grouping("matchMetadata.matchRuleName")), 
                        grouping("count", new Accumulator("$sum", 1)))
                .aggregate(outputType);
    }

    public List<Report> report() {
        ReportDAO reportDAO = new ReportDAO();
        Iterator<Report> iterator = reportDAO.report(ARecord.class, Report.class);

        List<Report> list = new ArrayList<>();
        while (iterator.hasNext()) {
            Report report = iterator.next();
            report.setRecordType("Side A");
            list.add(report);
        }

        Iterator<Report> iterator2 = reportDAO.report(BRecord.class, Report.class);

        while (iterator2.hasNext()) {
            Report report = iterator2.next();
            report.setRecordType("Side B");
            list.add(report);
        }

        return list;
    }
}
