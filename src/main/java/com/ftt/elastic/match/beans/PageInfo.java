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
public class PageInfo {

    private final Integer limit;
    private final Integer offset;

    public Integer getLimit() {
        return limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public PageInfo(Integer limit, Integer offset) {
        this.limit = limit;
        this.offset = offset;
    }

}
