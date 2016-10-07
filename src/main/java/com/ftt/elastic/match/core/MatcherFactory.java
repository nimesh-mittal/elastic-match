/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nimeshagarwal
 */
public final class MatcherFactory {

    private static final Map<String, Matcher> matchers = new HashMap();

    public static void register() {
        Matcher matcher = new MagicalMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new EqualsMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new GreaterThanMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new GreaterThanEqualsMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new LessThanMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new LessThanEqualsMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new SimilarMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new SimilarCaseInsensitiveMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
        
        matcher = new NotSimilarMatcher();
        matchers.put(matcher.getComparaorName(), matcher);
    }

    public static Matcher getInstance(String comparatorName) {

        if (matchers.isEmpty()) {
            register();
        }

        return matchers.get(comparatorName);
    }
}
