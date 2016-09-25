/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.beans;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nimeshagarwal
 */
public class MatchRule {

    private String name;
    private String priority;
    private String matchType;
    private List<String> keys;
    private List<MatchCondition> conditions;

    public MatchRule() {
        this.keys = new ArrayList<>();
        this.conditions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public List<MatchCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<MatchCondition> conditions) {
        this.conditions = conditions;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

}
