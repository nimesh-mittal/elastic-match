/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.engine;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.beans.MatchMetadata;
import com.ftt.elastic.match.beans.Record;
import com.ftt.elastic.match.db.ARecordDAO;
import com.ftt.elastic.match.db.BRecordDAO;
import com.ftt.elastic.match.utils.Constants;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.fluttercode.datafactory.impl.DataFactory;

/**
 *
 * @author nimeshagarwal
 */
public class DataImportEngine {

    private final List<String> keys;
    private final DataFactory df = new DataFactory();
    private final String[] colors = {"White", "Wheat", "Black"};
    private final String[] professions = {"HR", "IT", "Banking", "Insurance"};

    public DataImportEngine(List<String> keys) {
        this.keys = keys;
    }

    public static void main(String[] args) {
        //importData();

        List<String> keys = new ArrayList<>();
        keys.add("degree");
        keys.add("designation");
        DataImportEngine dataImportEngine = new DataImportEngine(keys);

        String aRecordsFilePath = "/opt/dev/data/a.csv";
        dataImportEngine.importCSVData(aRecordsFilePath, ARecord.class);

        String bRecordsFilePath = "/opt/dev/data/b.csv";
        dataImportEngine.importCSVData(bRecordsFilePath, BRecord.class);
    }

    private <T extends Record> void importCSVData(String filePath, Class<T> type) {

        try {
            List<T> aRecords = fileToRecords(filePath, type);
            System.out.println("imported records" + aRecords);
        } catch (IOException | InstantiationException | IllegalAccessException ex) {
            System.err.println("error in data import" + ex);
        }
    }

    private static void importData() {
        DataImportEngine dataImportEngine = new DataImportEngine(null);
        dataImportEngine.importSideAData(1000000);
        dataImportEngine.importSideBData(1000000);
    }

    public void importSideAData(int numberOfRecords) {
        long start = System.currentTimeMillis();
        ARecordDAO aRecordDAO = new ARecordDAO();

        for (int i = 0; i < numberOfRecords; i++) {
            aRecordDAO.create((ARecord) fillRecordWithDummyValues(new ARecord()));
        }

        long end = System.currentTimeMillis();
        System.out.println("time taken to import A records is " + (end - start) + " ms");
    }

    public void importSideBData(int numberOfRecords) {
        long start = System.currentTimeMillis();
        BRecordDAO bRecordDAO = new BRecordDAO();

        for (int i = 0; i < numberOfRecords; i++) {
            bRecordDAO.create((BRecord) fillRecordWithDummyValues(new BRecord()));
        }

        long end = System.currentTimeMillis();
        System.out.println("time taken to import B records is " + (end - start) + " ms");
    }

    public Record fillRecordWithDummyValues(Record record) {

        record.setDisplayId(df.getNumberBetween(0, 999999999) + "");
        record.setRecordId("A" + df.getNumberBetween(0, 999999999));

        String profession = df.getItem(professions, 80, "IT");
        record.setKey(profession);

        String jsonString = "";
        jsonString += "{";
        jsonString += "firstName: \"" + df.getFirstName() + "\",";
        jsonString += "lastName: \"" + df.getLastName() + "\",";
        jsonString += "emailId: \"" + df.getEmailAddress() + "\",";
        jsonString += "birthDate: \"" + df.getBirthDate() + "\",";
        jsonString += "city: \"" + df.getCity() + "\",";
        jsonString += "color: \"" + df.getItem(colors, 80, "White") + "\",";
        jsonString += "profession: \"" + profession + "\",";
        jsonString += "salary: " + (double) df.getNumberBetween(10, 20);
        jsonString += "}";

        DBObject dBObject = (DBObject) JSON.parse(jsonString);
        record.setdBObject(dBObject);

        record.setMatchMetadata(new MatchMetadata());
        record.getMatchMetadata().setStatus(Constants.MatchingStatus.UnMatch.name());
        return record;
    }

    public <T extends Record> List<T> fileToRecords(String filePath, Class<T> recordType) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {

        CSVReader reader = new CSVReader(new FileReader(filePath), ',', '\"');
        List<String[]> lines = reader.readAll();

        List<String> headers = Arrays.asList(lines.get(0));
        List<Integer> keyPositions = getKeyPositions(headers, keys);

        List<T> records = new ArrayList<>();

        boolean skipFirstLine = true;
        for (String[] line : lines) {
            if (skipFirstLine) {
                skipFirstLine = false;
                continue;
            }

            T aRecord = recordType.newInstance();

            aRecord.setDisplayId(line[0]);

            String key = "";
            for (Integer position : keyPositions) {
                key += "_" + line[position];
            }
            aRecord.setKey(key);

            aRecord.setRecordId(UUID.randomUUID().toString());

            String json = "{";
            int i = 0;
            for (String field : headers) {
                json += "\"" + field + "\":\"" + line[i++] + "\"";
                if (headers.size() != i) {
                    json += ",";
                }
            }
            json += "}";

            DBObject dBObject = (DBObject) JSON.parse(json);
            aRecord.setdBObject(dBObject);
            records.add(aRecord);
        }
        return records;
    }

    public List<Integer> getKeyPositions(List<String> headers, List<String> keys) {
        List<Integer> integers = new ArrayList<>();

        for (String key : keys) {
            int i = 0;
            for (String header : headers) {
                if (header.equalsIgnoreCase(key)) {
                    integers.add(i);
                }
                i++;
            }
        }
        return integers;
    }
}
