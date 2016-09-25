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
public class MatchMetadata {

    private String status;
    private List<String> matchIds;
    private String matchRuleName;

    public MatchMetadata() {
        this.matchIds = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMatchIds() {
        return matchIds;
    }

    public void setMatchIds(List<String> matchIds) {
        this.matchIds = matchIds;
    }

    public String getMatchRuleName() {
        return matchRuleName;
    }

    public void setMatchRuleName(String matchRuleName) {
        this.matchRuleName = matchRuleName;
    }

}
