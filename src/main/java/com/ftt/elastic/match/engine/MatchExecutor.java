/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.engine;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.beans.MatchCondition;
import com.ftt.elastic.match.utils.Constants;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author nimeshagarwal
 */
public class MatchExecutor {

    public static Boolean match(ARecord aRecord, BRecord bRecord, List<MatchCondition> matchConditions) {
        boolean finalStatus = true;
        for (MatchCondition matchCondition : matchConditions) {
            if (isStringBased(matchCondition.getComparator())) {
                String fieldValue1 = (String) aRecord.getdBObject().get(matchCondition.getAttribute());
                String fieldValue2 = (String) bRecord.getdBObject().get(matchCondition.getAttribute());
                finalStatus = match(matchCondition, fieldValue1, fieldValue2);
            } else if (isNumeric(matchCondition.getComparator())) {
                String fieldValue1Str = (String) aRecord.getdBObject().get(matchCondition.getAttribute());
                String fieldValue2Str = (String) bRecord.getdBObject().get(matchCondition.getAttribute());
                Double fieldValue1 = Double.parseDouble(fieldValue1Str == null ? "0" : fieldValue1Str);
                Double fieldValue2 = Double.parseDouble(fieldValue2Str == null ? "0" : fieldValue2Str);
                finalStatus = match(matchCondition, fieldValue1, fieldValue2);
            }

            if (!finalStatus) {
                break;//found mis-matchManyToMany in one of the condition
            }
        }
        return finalStatus;
    }

    /**
     * For number based matchManyToMany
     *
     * @param matchCondition
     * @param fieldValue1
     * @param fieldValue2
     * @return
     * @throws NumberFormatException
     */
    private static boolean match(MatchCondition matchCondition, Double fieldValue1, Double fieldValue2) throws NumberFormatException {
        Double difference = Math.abs(fieldValue1 - fieldValue2);
        Double threshold = Double.parseDouble(matchCondition.getThreshold());
        int compare = fieldValue1.compareTo(fieldValue2);

        boolean finalStatus = false;

        if (Constants.MatchOperators.Equals.equalsIgnoreCase(matchCondition.getComparator())) {
            finalStatus = difference <= threshold;
        } else if (Constants.MatchOperators.Greaterthan.equalsIgnoreCase(matchCondition.getComparator())) {
            finalStatus = (compare > 0);
        } else if (Constants.MatchOperators.GreaterthanEquals.equalsIgnoreCase(matchCondition.getComparator())) {
            finalStatus = (compare >= 0);
        } else if (Constants.MatchOperators.Lessthan.equalsIgnoreCase(matchCondition.getComparator())) {
            finalStatus = (compare < 0);
        } else if (Constants.MatchOperators.LessthanEquals.equalsIgnoreCase(matchCondition.getComparator())) {
            finalStatus = (compare <= 0);
        }
        return finalStatus;
    }

    /**
     * For string based matchManyToMany
     *
     * @param matchCondition
     * @param fieldValue1
     * @param fieldValue2
     * @return
     */
    private static boolean match(MatchCondition matchCondition, String fieldValue1, String fieldValue2) {
        if (Constants.MatchOperators.Similar.equalsIgnoreCase(matchCondition.getComparator())) {
            return fieldValue1.equals(fieldValue2);
        } else if (Constants.MatchOperators.SimilarCaseInsensitive.equalsIgnoreCase(matchCondition.getComparator())) {
            return fieldValue1.equalsIgnoreCase(fieldValue2);
        } else if (Constants.MatchOperators.NotSimilar.equalsIgnoreCase(matchCondition.getComparator())) {
            return !fieldValue1.equals(fieldValue2);
        }
        return false;
    }

    private static boolean isNumeric(String comparator) {
        return comparator.equalsIgnoreCase(Constants.MatchOperators.Equals)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.Greaterthan)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.GreaterthanEquals)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.Lessthan)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.LessthanEquals);
    }

    private static boolean isStringBased(String comparator) {
        return comparator.equalsIgnoreCase(Constants.MatchOperators.Similar)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.SimilarCaseInsensitive)
                || comparator.equalsIgnoreCase(Constants.MatchOperators.NotSimilar);
    }

    static boolean matchManyToMany(List<ARecord> matchingARecords, List<BRecord> matchingBRecords, List<MatchCondition> matchConditions) {
        boolean finalStatus = true;
        for (MatchCondition matchCondition : matchConditions) {
            if (isStringBased(matchCondition.getComparator())) {

                List<String> aRecordFieldValues = matchingARecords
                        .stream()
                        .map(aRecord -> (String) aRecord.getdBObject().get(matchCondition.getAttribute()))
                        .collect(Collectors.toList());

                List<String> bRecordFieldValues = matchingBRecords
                        .stream()
                        .map(aRecord -> (String) aRecord.getdBObject().get(matchCondition.getAttribute()))
                        .collect(Collectors.toList());

                finalStatus = stringMatch(matchCondition, aRecordFieldValues, bRecordFieldValues);
            } else if (isNumeric(matchCondition.getComparator())) {

                Double fieldValue1 = matchingARecords
                        .stream()
                        .map(aRecord -> (Double) aRecord.getdBObject().get(matchCondition.getAttribute()))
                        .reduce(0.0, Double::sum);

                Double fieldValue2 = matchingBRecords
                        .stream()
                        .map(bRecord -> (Double) bRecord.getdBObject().get(matchCondition.getAttribute()))
                        .reduce(0.0, Double::sum);

                finalStatus = match(matchCondition, fieldValue1, fieldValue2);
            }

            if (!finalStatus) {
                break;//found mis-match ManyToMany in one of the condition
            }
        }
        return finalStatus;
    }

    private static boolean stringMatch(MatchCondition matchCondition, List<String> aRecordFieldValues, List<String> bRecordFieldValues) {
        if (Constants.MatchOperators.Similar.equalsIgnoreCase(matchCondition.getComparator())) {
            String matchValue = aRecordFieldValues.get(0);
            for (String aRecord : aRecordFieldValues) {
                if (!matchValue.equals(aRecord)) {
                    return false;
                }
            }
            for (String bRecord : bRecordFieldValues) {
                if (!matchValue.equals(bRecord)) {
                    return false;
                }
            }
            return true;
        } else if (Constants.MatchOperators.SimilarCaseInsensitive.equalsIgnoreCase(matchCondition.getComparator())) {
            String matchValue = aRecordFieldValues.get(0);
            for (String aRecord : aRecordFieldValues) {
                if (!matchValue.equalsIgnoreCase(aRecord)) {
                    return false;
                }
            }
            for (String bRecord : bRecordFieldValues) {
                if (!matchValue.equalsIgnoreCase(bRecord)) {
                    return false;
                }
            }
            return true;
        } else if (Constants.MatchOperators.NotSimilar.equalsIgnoreCase(matchCondition.getComparator())) {
            String matchValue = aRecordFieldValues.get(0);
            for (String aRecord : aRecordFieldValues) {
                if (!matchValue.equals(aRecord)) {
                    return false;
                }
            }
            for (String bRecord : bRecordFieldValues) {
                if (matchValue.equals(bRecord)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
