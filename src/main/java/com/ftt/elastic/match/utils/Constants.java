/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.utils;

/**
 *
 * @author nimeshagarwal
 */
public class Constants {

    public static final String Zero = "0";

    public enum MatchingTypes {
        OneToOne, OneToMany, ManyToMany
    }

    public enum MatchingStatus {
        Match, UnMatch, NotMatched
    }

    public interface MatchOperators {

        public final String Greaterthan = ">";
        public final String GreaterthanEquals = ">=";
        public final String Lessthan = "<";
        public final String LessthanEquals = "<=";
        public final String Equals = "=";
        public final String Similar = "==";
        public final String NotSimilar = "!=";
        public final String SimilarCaseInsensitive = "i==";
    }

    public interface Settings {
        public final String MATCH_BATCH_DELAY = "match.batch.delay";
        public final String MATCH_BUCKET_MAX_SIZE = "match.bucket.max-size";
    }
}
