/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.beans.MatchCondition;
import com.ftt.elastic.match.utils.Constants;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author nimeshagarwal
 */
public class MatchConditionExecutor {

    //Match one to one
    public static Boolean match(ARecord aRecord, BRecord bRecord, List<MatchCondition> matchConditions) {
        boolean matchStatus = false;
        for (MatchCondition matchCondition : matchConditions) {

            matchStatus = match(matchCondition, aRecord, bRecord);

            if (!matchStatus) {
                break;
            }
        }
        return matchStatus;
    }

    //Match Many to Many
    public static boolean match(List<ARecord> aRecords, List<BRecord> bRecords, List<MatchCondition> matchConditions) {
        boolean matchStatus = true;
        for (MatchCondition matchCondition : matchConditions) {

            matchStatus = match(matchCondition, aRecords, bRecords);

            if (!matchStatus) {
                break;
            }
        }
        return matchStatus;
    }

    private static boolean match(MatchCondition matchCondition, ARecord aRecord, BRecord bRecord) throws NumberFormatException {
        String stringValue1 = (String) aRecord.getdBObject().get(matchCondition.getAttribute());
        String stringValue2 = (String) bRecord.getdBObject().get(matchCondition.getAttribute());

        if (StringMatcher.isStringComparator(matchCondition.getComparator())) {
            return StringMatcher.match(matchCondition, stringValue1, stringValue2);
        } else if (NumericMatcher.isNumericComparator(matchCondition.getComparator())) {
            Double value1 = Double.parseDouble(Objects.toString(stringValue1, Constants.Zero));
            Double value2 = Double.parseDouble(Objects.toString(stringValue2, Constants.Zero));
            return NumericMatcher.match(matchCondition, value1, value2);
        }
        return false;
    }

    private static boolean match(MatchCondition matchCondition, List<ARecord> aRecords, List<BRecord> bRecords) {
        if (StringMatcher.isStringComparator(matchCondition.getComparator())) {

            List<String> values1 = aRecords.stream()
                    .map(aRecord -> (String) aRecord.getdBObject().get(matchCondition.getAttribute()))
                    .collect(Collectors.toList());

            List<String> values2 = bRecords.stream()
                    .map(aRecord -> (String) aRecord.getdBObject().get(matchCondition.getAttribute()))
                    .collect(Collectors.toList());

            return StringMatcher.match(matchCondition, values1, values2);
        } else if (NumericMatcher.isNumericComparator(matchCondition.getComparator())) {

            List<Double> values1 = aRecords.stream()
                    .map(aRecord -> (Double) aRecord.getdBObject().get(matchCondition.getAttribute()))
                    .collect(Collectors.toList());

            List<Double> values2 = bRecords.stream()
                    .map(bRecord -> (Double) bRecord.getdBObject().get(matchCondition.getAttribute()))
                    .collect(Collectors.toList());

            return NumericMatcher.match(matchCondition, values1, values2);
        }
        return false;
    }
}
