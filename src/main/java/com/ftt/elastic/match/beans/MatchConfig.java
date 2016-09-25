/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.beans;

import java.util.ArrayList;
import java.util.List;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author nimeshagarwal
 */
@Entity("match-config")
public class MatchConfig {

    @Id
    private String name;
    private String sideAName;
    private String sideBName;
    private String primarySide;

    private String sideADirectoryPath;
    private String sideBDirectoryPath;

    private String displayId;

    private List<String> identifingKeys;
    private List<MatchRule> matchingRules;

    public MatchConfig() {
        this.identifingKeys = new ArrayList<>();
        this.matchingRules = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSideAName() {
        return sideAName;
    }

    public void setSideAName(String sideAName) {
        this.sideAName = sideAName;
    }

    public String getSideBName() {
        return sideBName;
    }

    public void setSideBName(String sideBName) {
        this.sideBName = sideBName;
    }

    public String getPrimarySide() {
        return primarySide;
    }

    public void setPrimarySide(String primarySide) {
        this.primarySide = primarySide;
    }

    public List<String> getIdentifingKeys() {
        return identifingKeys;
    }

    public void setIdentifingKeys(List<String> identifingKeys) {
        this.identifingKeys = identifingKeys;
    }

    public List<MatchRule> getMatchingRules() {
        return matchingRules;
    }

    public void setMatchingRules(List<MatchRule> matchingRules) {
        this.matchingRules = matchingRules;
    }

    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayId(String displayId) {
        this.displayId = displayId;
    }

    public String getSideADirectoryPath() {
        return sideADirectoryPath;
    }

    public String getSideBDirectoryPath() {
        return sideBDirectoryPath;
    }

    public void setSideADirectoryPath(String sideADirectoryPath) {
        this.sideADirectoryPath = sideADirectoryPath;
    }

    public void setSideBDirectoryPath(String sideBDirectoryPath) {
        this.sideBDirectoryPath = sideBDirectoryPath;
    }

}
