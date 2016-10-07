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
public class MagicalMatcher implements Matcher {

    @Override
    public String getComparaorName() {
        return "=m";
    }

    @Override
    public boolean match(String value1, String value2, String threshold) {
        System.out.println("its super magical because it comes from plugin folder");
        return (value1.hashCode() + value2.hashCode()) % 2 == 0;
    }

    @Override
    public boolean match(List<String> value1, List<String> value2, String threshold) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
