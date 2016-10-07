/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import java.util.List;

/**
 *
 * @author nimeshagarwal
 */
public interface Matcher {

    public String getComparaorName();

    public boolean match(String value1, String value2, String threshold);

    public boolean match(List<String> value1, List<String> value2, String threshold);
}
