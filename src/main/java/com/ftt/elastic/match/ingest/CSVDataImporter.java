/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.ingest;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.beans.MatchConfig;
import com.ftt.elastic.match.beans.MatchMetadata;
import com.ftt.elastic.match.beans.Record;
import com.ftt.elastic.match.db.ARecordDAO;
import com.ftt.elastic.match.db.BRecordDAO;
import com.ftt.elastic.match.db.MatchConfigDAO;
import com.ftt.elastic.match.exception.BadConfigException;
import com.ftt.elastic.match.utils.Constants;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author nimeshagarwal
 */
public class CSVDataImporter {

    private List<String> keys;
    private String displayId;
    private String sideName;
    private String matchName;

    public CSVDataImporter(String directoryPath) throws BadConfigException {
        setMatchProperties(directoryPath);
    }

    public final void setMatchProperties(String directoryPath) throws BadConfigException {
        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("sideADirectoryPath", directoryPath);
        List<MatchConfig> configsForSideA = matchConfigDAO.filter(searchCriteria, null, null);

        if (configsForSideA != null && configsForSideA.size() == 1) {
            MatchConfig config = configsForSideA.get(0);
            keys = config.getIdentifingKeys();
            displayId = config.getDisplayId();
            sideName = "SideA";
            matchName = config.getName();
        } else {
            searchCriteria.clear();
            searchCriteria.put("sideBDirectoryPath", directoryPath);
            List<MatchConfig> configsForSideB = matchConfigDAO.filter(searchCriteria, null, null);

            if (configsForSideB != null && configsForSideB.size() == 1) {
                MatchConfig config = configsForSideB.get(0);
                keys = config.getIdentifingKeys();
                displayId = config.getDisplayId();
                sideName = "SideB";
                matchName = config.getName();
            } else {
                throw new BadConfigException("unable to find unique match config for directory " + directoryPath);
            }
        }
    }

    public boolean importFile(String filePath) throws IOException, FileNotFoundException, InstantiationException, IllegalAccessException {

        if (sideName.equalsIgnoreCase("SideA")) {
            List<ARecord> records = fileToRecords(filePath, ARecord.class);
            ARecordDAO aRecordDAO = new ARecordDAO();
            records.stream().forEach(aRecordDAO::create);
        } else if (sideName.equalsIgnoreCase("SideB")) {
            List<BRecord> records = fileToRecords(filePath, BRecord.class);
            BRecordDAO bRecordDAO = new BRecordDAO();
            records.stream().forEach(bRecordDAO::create);
        }
        return true;
    }

    public <T extends Record> List<T> fileToRecords(String filePath, Class<T> recordType) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException {

        CSVReader reader = new CSVReader(new FileReader(filePath), ',', '\"');
        List<String[]> lines = reader.readAll();

        List<String> headers = Arrays.asList(lines.get(0));
        List<Integer> keyPositions = getKeyPositions(headers, keys);
        int displayIdPosition = getDisplayIdPosition(headers, displayId);
        List<T> records = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String[] line = lines.get(i);

            T record = recordType.newInstance();

            record.setDisplayId(line[displayIdPosition]);

            String key = getKey(keyPositions, line);
            record.setKey(key);

            record.setRecordId(UUID.randomUUID().toString());

            String json = getJson(headers, line);
            DBObject dBObject = (DBObject) JSON.parse(json);
            record.setdBObject(dBObject);
            record.setMatchName(matchName);

            record.setMatchMetadata(new MatchMetadata());
            record.getMatchMetadata().setStatus(Constants.MatchingStatus.UnMatch.name());

            records.add(record);
        }

        return records;
    }

    private String getKey(List<Integer> keyPositions, String[] line) {
        String key = "";
        for (Integer position : keyPositions) {
            key += "_" + line[position];
        }
        return key;
    }

    private String getJson(List<String> headers, String[] line) {
        String json = "{";
        int i = 0;
        for (String field : headers) {
            json += "\"" + field + "\":\"" + getValue(line[i++]) + "\"";
            if (headers.size() != i) {
                json += ",";
            }
        }
        json += "}";
        return json;
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

    private int getDisplayIdPosition(List<String> headers, String displayId) {
        int position = 0;
        for (String field : headers) {
            if (field.equalsIgnoreCase(displayId)) {
                return position;
            }
            position++;
        }
        return -1;
    }

    private String getValue(String string) {
        if (NumberUtils.isNumber(string) && StringUtils.isNumeric(string)) {
            return string + ".0";// convert int to double
        }
        return string;
    }
}
