/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.core;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.beans.MatchCondition;
import java.util.List;
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
        String value1 = (String) aRecord.getdBObject().get(matchCondition.getAttribute());
        String value2 = (String) bRecord.getdBObject().get(matchCondition.getAttribute());

        Matcher matcher = MatcherFactory.getInstance(matchCondition.getComparator());
        return matcher.match(value1, value2, matchCondition.getThreshold());
    }

    private static boolean match(MatchCondition matchCondition, List<ARecord> aRecords, List<BRecord> bRecords) {

        List<String> values1 = aRecords.stream()
                .map(aRecord -> (String) aRecord.getdBObject().get(matchCondition.getAttribute()))
                .collect(Collectors.toList());

        List<String> values2 = bRecords.stream()
                .map(aRecord -> (String) aRecord.getdBObject().get(matchCondition.getAttribute()))
                .collect(Collectors.toList());

        Matcher matcher = MatcherFactory.getInstance(matchCondition.getComparator());
        return matcher.match(values1, values2, matchCondition.getThreshold());
    }
}
