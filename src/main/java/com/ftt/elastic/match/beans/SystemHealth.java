/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.beans;

import java.util.Date;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author nimeshagarwal
 */
@Entity
public class SystemHealth {

    @Id
    private String id;
    private String ImportEngine;
    private String WebEngine;
    private String MatchingEngine;
    private Date lastRecorded;

    public String getImportEngine() {
        return ImportEngine;
    }

    public void setImportEngine(String ImportEngine) {
        this.ImportEngine = ImportEngine;
    }

    public String getWebEngine() {
        return WebEngine;
    }

    public void setWebEngine(String WebEngine) {
        this.WebEngine = WebEngine;
    }

    public String getMatchingEngine() {
        return MatchingEngine;
    }

    public void setMatchingEngine(String MatchingEngine) {
        this.MatchingEngine = MatchingEngine;
    }

    public Date getLastRecorded() {
        return lastRecorded;
    }

    public void setLastRecorded(Date lastRecorded) {
        this.lastRecorded = lastRecorded;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
