/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.beans;

/**
 *
 * @author nimeshagarwal
 */
public class MatchCondition {

    private String attribute;
    private String comparator;
    private String threshold;

    public MatchCondition(String attribute, String comparator, String threshold) {
        this.attribute = attribute;
        this.comparator = comparator;
        this.threshold = threshold;
    }

    public MatchCondition() {
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getComparator() {
        return comparator;
    }

    public void setComparator(String comparator) {
        this.comparator = comparator;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

}
