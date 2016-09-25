/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.beans;

import com.mongodb.DBObject;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

/**
 *
 * @author nimeshagarwal
 */
public class Record {

    @Id
    private String recordId;

    @Property("key")
    private String key;

    @Property("displayId")
    private String displayId;

    private String matchName;

    @Embedded
    private MatchMetadata matchMetadata;

    @Property("fields")
    private DBObject dBObject;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MatchMetadata getMatchMetadata() {
        return matchMetadata;
    }

    public void setMatchMetadata(MatchMetadata matchMetadata) {
        this.matchMetadata = matchMetadata;
    }

    public DBObject getdBObject() {
        return dBObject;
    }

    public void setdBObject(DBObject dBObject) {
        this.dBObject = dBObject;
    }

    @Override
    public String toString() {
        return "Record{" + "recordId=" + recordId + ", key=" + key + ", displayId=" + displayId + ", matchMetadata=" + matchMetadata + ", dBObject=" + dBObject + '}';
    }

    public String getMatchName() {
        return matchName;
    }

    public void setMatchName(String matchName) {
        this.matchName = matchName;
    }

}
