/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import com.ftt.elastic.match.utils.Constants;
import java.util.List;
import java.util.function.Predicate;

/**
 *
 * @author nimeshagarwal
 */
public class NotSimilarMatcher implements Matcher {

    @Override
    public String getComparaorName() {
        return Constants.MatchOperators.NotSimilar;
    }

    @Override
    public boolean match(String _value1, String _value2, String _threshold) {
        return !_value1.equals(_value2);
    }

    @Override
    public boolean match(List<String> list1, List<String> list2, String _threshold) {
        String matchValue = list1.get(0);
        Predicate predicate = v -> matchValue.equals(v);
        return list1.stream().allMatch(predicate) && list2.stream().allMatch(predicate.negate());
    }

}
