/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import com.ftt.elastic.match.beans.MatchCondition;
import com.ftt.elastic.match.utils.Constants;
import java.util.List;
import java.util.function.Predicate;
import org.pmw.tinylog.Logger;

/**
 *
 * @author nimeshagarwal
 */
public class StringMatcher {

    //Match one to one
    public static boolean match(MatchCondition matchCondition, String value1, String value2) {

        switch (matchCondition.getComparator()) {
            case Constants.MatchOperators.Similar: {
                return value1.equals(value2);
            }
            case Constants.MatchOperators.SimilarCaseInsensitive: {
                return value1.equalsIgnoreCase(value2);
            }
            case Constants.MatchOperators.NotSimilar: {
                return !value1.equals(value2);
            }
        }

        Logger.error("comparision {} not supported", matchCondition.getComparator());
        return false;
    }

    //match many to many
    public static boolean match(MatchCondition matchCondition, List<String> values1, List<String> values2) {

        String matchValue = values1.get(0);

        switch (matchCondition.getComparator()) {
            case Constants.MatchOperators.Similar: {
                Predicate predicate = v -> matchValue.equals(v);
                boolean finalStatus = values1.stream().allMatch(predicate);
                finalStatus = finalStatus && values2.stream().allMatch(predicate);
                return finalStatus;
            }
            case Constants.MatchOperators.SimilarCaseInsensitive: {
                Predicate predicate = v -> matchValue.equalsIgnoreCase(v.toString());
                boolean finalStatus = values1.stream().allMatch(predicate);
                finalStatus = finalStatus && values2.stream().allMatch(predicate);
                return finalStatus;
            }
            case Constants.MatchOperators.NotSimilar: {
                Predicate predicate = v -> matchValue.equals(v);
                boolean finalStatus = values1.stream().allMatch(predicate);
                finalStatus = finalStatus && values2.stream().allMatch(predicate.negate());
                return finalStatus;
            }
        }

        Logger.error("comparision {} not supported", matchCondition.getComparator());
        return false;
    }

    public static boolean isStringComparator(String comparator) {
        return comparator.equalsIgnoreCase(Constants.MatchOperators.Similar)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.SimilarCaseInsensitive)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.NotSimilar);
    }

}
