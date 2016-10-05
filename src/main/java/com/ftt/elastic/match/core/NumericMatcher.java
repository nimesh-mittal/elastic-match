/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import com.ftt.elastic.match.beans.MatchCondition;
import com.ftt.elastic.match.utils.Constants;
import java.util.List;
import org.pmw.tinylog.Logger;

/**
 *
 * @author nimeshagarwal
 */
public class NumericMatcher {

    public static boolean match(MatchCondition matchCondition, Double value1, Double value2) throws NumberFormatException {
        Double difference = Math.abs(value1 - value2);
        Double threshold = Double.parseDouble(matchCondition.getThreshold());
        int compare = value1.compareTo(value2);

        switch (matchCondition.getComparator()) {
            case Constants.MatchOperators.Equals: {
                return difference <= threshold;
            }
            case Constants.MatchOperators.Greaterthan: {
                return compare > 0;
            }
            case Constants.MatchOperators.GreaterthanEquals: {
                return compare >= 0;
            }
            case Constants.MatchOperators.Lessthan: {
                return compare < 0;
            }
            case Constants.MatchOperators.LessthanEquals: {
                return compare <= 0;
            }
        }

        Logger.error("comparision {} not supported", matchCondition.getComparator());
        return false;
    }

    public static boolean match(MatchCondition matchCondition, List<Double> values1, List<Double> values2) {
        Double value1 = values1.stream().reduce(0.0, Double::sum);
        Double value2 = values2.stream().reduce(0.0, Double::sum);
        return match(matchCondition, value1, value2);
    }

    public static boolean isNumericComparator(String comparator) {
        return comparator.equalsIgnoreCase(Constants.MatchOperators.Equals)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.Greaterthan)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.GreaterthanEquals)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.Lessthan)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.LessthanEquals);
    }

}
