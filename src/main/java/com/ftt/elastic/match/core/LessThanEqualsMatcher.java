/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import com.ftt.elastic.match.utils.Constants;
import java.util.List;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author nimeshagarwal
 */
public class LessThanEqualsMatcher implements Matcher {

    @Override
    public String getComparaorName() {
        return Constants.MatchOperators.LessthanEquals;
    }

    @Override
    public boolean match(String _value1, String _value2, String _threshold) {
        if (!preCondition(_value1, _value2)) {
            return false;
        }

        Double value1 = Double.parseDouble(_value1);
        Double value2 = Double.parseDouble(_value2);
        Double threshold = Double.parseDouble(_threshold);
        return match(value1, value2, threshold);
    }

    private boolean match(Double value1, Double value2, Double threshold) {
        int compare = value1.compareTo(value2);
        return (compare + threshold) <= 0;
    }

    @Override
    public boolean match(List<String> value1, List<String> value2, String _threshold) {
        if (!preCondition(value1, value2)) {
            return false;
        }

        Double v1 = value1.stream()
                .map(v -> Double.parseDouble(v))
                .reduce(0.0, Double::sum);

        Double v2 = value2.stream()
                .map(v -> Double.parseDouble(v))
                .reduce(0.0, Double::sum);

        Double threshold = Double.parseDouble(_threshold);
        return match(v1, v2, threshold);
    }

    private boolean preCondition(List<String> value1, List<String> value2) {
        boolean isList1Valid = value1.stream().allMatch(v -> NumberUtils.isNumber(v));
        boolean isList2Valid = value2.stream().allMatch(v -> NumberUtils.isNumber(v));
        return isList1Valid && isList2Valid;
    }

    private boolean preCondition(String _value1, String _value2) {
        return NumberUtils.isNumber(_value1) && NumberUtils.isNumber(_value2);
    }
}
