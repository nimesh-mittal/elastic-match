/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.engine;

import com.ftt.elastic.match.core.MatchConditionExecutor;
import com.ftt.elastic.match.startup.StartupSettings;
import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.BRecord;
import com.ftt.elastic.match.beans.MatchCondition;
import com.ftt.elastic.match.beans.MatchConfig;
import com.ftt.elastic.match.beans.MatchRule;
import com.ftt.elastic.match.beans.PageInfo;
import com.ftt.elastic.match.db.ARecordDAO;
import com.ftt.elastic.match.db.BRecordDAO;
import com.ftt.elastic.match.db.MatchConfigDAO;
import com.ftt.elastic.match.utils.Constants;
import com.ftt.elastic.match.utils.LogUtils;
import com.ftt.elastic.match.utils.PropertiesRepo;
import com.ftt.elastic.match.utils.SystemHealthUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.pmw.tinylog.Logger;

/**
 *
 * @author nimeshagarwal
 */
public class MatchingEngine {

    public MatchingEngine() {
        StartupSettings.initEngine();
    }

    public static void main(String[] args) throws InterruptedException {
        MatchingEngine matchingEngine = new MatchingEngine();
        matchingEngine.start();
    }

    public void start() throws InterruptedException {
        while (true) {
            long start = System.currentTimeMillis();
            MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
            List<MatchConfig> matchConfigs = matchConfigDAO.filter(new HashMap<>(), null, null);

            for (MatchConfig matchConfig : matchConfigs) {
                LogUtils.logStart();
                Logger.info("running matching for tenant {}", matchConfig.getName());

                List<String> keys = getDistinctKeys(matchConfig.getName());
                for (String key : keys) {
                    for (MatchRule matchRule : matchConfig.getMatchingRules()) {
                        Logger.info("[{}] executing rule {}", key, matchRule.getName());
                        List<ARecord> aRecords = getARecords(key, matchConfig.getName());
                        List<BRecord> bRecords = getBRecords(key, matchConfig.getName());
                        Logger.info("[{}] ARecords : {}", key, aRecords.stream().map(a -> a.getDisplayId()).collect(Collectors.toList()));
                        Logger.info("[{}] BRecords : {}", key, bRecords.stream().map(b -> b.getDisplayId()).collect(Collectors.toList()));
                        match(aRecords, bRecords, matchRule);
                    }
                }
                long end = System.currentTimeMillis();
                Logger.info("total time taken is {} ms", (end - start));
                LogUtils.logEnd();
                SystemHealthUtils.create("Running", "matching");
            }
            Thread.sleep(PropertiesRepo.getInt(Constants.Settings.MATCH_BATCH_DELAY) * 1000);
        }
    }

    private List<String> getDistinctKeys(String matchName) {
        ARecordDAO aRecordDAO = new ARecordDAO();
        List<String> keys = aRecordDAO.distinct(matchName);
        return keys;
    }

    private List<ARecord> getARecords(String key, String matchName) {
        ARecordDAO aRecordDAO = new ARecordDAO();
        PageInfo pageInfo = new PageInfo(PropertiesRepo.getInt(Constants.Settings.MATCH_BUCKET_MAX_SIZE), 0);

        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("key =", key);
        searchCriteria.put("matchMetadata.status =", Constants.MatchingStatus.UnMatch.name());
        searchCriteria.put("matchName =", matchName);

        return aRecordDAO.filter(searchCriteria, pageInfo, "key");
    }

    private List<BRecord> getBRecords(String key, String matchName) {
        BRecordDAO bRecordDAO = new BRecordDAO();

        PageInfo pageInfo = new PageInfo(PropertiesRepo.getInt(Constants.Settings.MATCH_BUCKET_MAX_SIZE), 0);

        Map<String, Object> searchCriteria = new HashMap<>();
        searchCriteria.put("key =", key);
        searchCriteria.put("matchMetadata.status =", Constants.MatchingStatus.UnMatch.name());
        searchCriteria.put("matchName =", matchName);

        return bRecordDAO.filter(searchCriteria, pageInfo, "key");
    }

    private void match(List<ARecord> aRecords, List<BRecord> bRecords, MatchRule matchRule) {

        Map<String, List<ARecord>> matchKeyARecordMap = constructARecordMap(aRecords, matchRule);
        Map<String, List<BRecord>> matchKeyBRecordMap = constructBRecordMap(bRecords, matchRule);

        for (String key : matchKeyARecordMap.keySet()) {
            List<ARecord> matchingARecords = matchKeyARecordMap.get(key);
            List<BRecord> matchingBRecords = matchKeyBRecordMap.get(key);

            if (Objects.isNull(matchingBRecords)) {
                matchingBRecords = new ArrayList<>();
            }

            if (Constants.MatchingTypes.OneToOne.name().equalsIgnoreCase(matchRule.getMatchType())) {
                matchOneToOne(matchingARecords, matchingBRecords, matchRule.getConditions(), matchRule.getName());
            } else if (Constants.MatchingTypes.OneToMany.name().equalsIgnoreCase(matchRule.getMatchType())) {
                matchManyToMany(matchingARecords, matchingBRecords, matchRule.getConditions(), matchRule.getName());
            }
        }
    }

    private Map<String, List<BRecord>> constructBRecordMap(List<BRecord> bRecords, MatchRule matchRule) {
        Map<String, List<BRecord>> matchKeyBRecordMap = new HashMap<>();
        for (BRecord bRecord : bRecords) {
            String matchKey = "";
            for (String key : matchRule.getKeys()) {
                matchKey += bRecord.getdBObject().get(key);
            }
            if (matchKeyBRecordMap.get(matchKey) == null) {
                matchKeyBRecordMap.put(matchKey, new ArrayList<>());
            }
            matchKeyBRecordMap.get(matchKey).add(bRecord);
        }
        return matchKeyBRecordMap;
    }

    private Map<String, List<ARecord>> constructARecordMap(List<ARecord> aRecords, MatchRule matchRule) {
        Map<String, List<ARecord>> matchKeyARecordMap = new HashMap<>();

        for (ARecord aRecord : aRecords) {
            String matchKey = "";
            for (String key : matchRule.getKeys()) {
                matchKey += aRecord.getdBObject().get(key);
            }
            if (matchKeyARecordMap.get(matchKey) == null) {
                matchKeyARecordMap.put(matchKey, new ArrayList<>());
            }
            matchKeyARecordMap.get(matchKey).add(aRecord);
        }
        return matchKeyARecordMap;
    }

    private void matchOneToOne(List<ARecord> aRecords, List<BRecord> bRecords, List<MatchCondition> matchConditions, String ruleName) {
        for (ARecord aRecord : aRecords) {
            Iterator<BRecord> iterator = bRecords.iterator();
            while (iterator.hasNext()) {
                BRecord bRecord = iterator.next();
                if (MatchConditionExecutor.match(aRecord, bRecord, matchConditions)) {
                    updateMatchInfo(aRecord, bRecord, ruleName);
                    iterator.remove();
                    break;
                }
            }
        }
    }

    private void matchManyToMany(List<ARecord> matchingARecords, List<BRecord> matchingBRecords, List<MatchCondition> matchConditions, String ruleName) {
        if (MatchConditionExecutor.match(matchingARecords, matchingBRecords, matchConditions)) {
            updateMatchInfo(matchingARecords, matchingBRecords, ruleName);
        }
    }

    private void updateMatchInfo(ARecord aRecord, BRecord bRecord, String ruleName) {
        aRecord.getMatchMetadata().setStatus(Constants.MatchingStatus.Match.name());
        aRecord.getMatchMetadata().getMatchIds().add(bRecord.getDisplayId());
        aRecord.getMatchMetadata().setMatchRuleName(ruleName);

        bRecord.getMatchMetadata().setStatus(Constants.MatchingStatus.Match.name());
        bRecord.getMatchMetadata().getMatchIds().add(aRecord.getDisplayId());
        bRecord.getMatchMetadata().setMatchRuleName(ruleName);

        ARecordDAO aRecordDAO = new ARecordDAO();
        Map<String, Object> aRecordFieldsToUpdate = new HashMap<>();
        aRecordFieldsToUpdate.put("matchMetadata.status", Constants.MatchingStatus.Match.name());
        aRecordFieldsToUpdate.put("matchMetadata.matchIds", aRecord.getMatchMetadata().getMatchIds());
        aRecordFieldsToUpdate.put("matchMetadata.matchRuleName", ruleName);
        aRecordDAO.update(aRecord.getRecordId(), aRecordFieldsToUpdate);

        BRecordDAO bRecordDAO = new BRecordDAO();
        Map<String, Object> bRecordFieldsToUpdate = new HashMap<>();
        bRecordFieldsToUpdate.put("matchMetadata.status", Constants.MatchingStatus.Match.name());
        bRecordFieldsToUpdate.put("matchMetadata.matchIds", bRecord.getMatchMetadata().getMatchIds());
        bRecordFieldsToUpdate.put("matchMetadata.matchRuleName", ruleName);
        bRecordDAO.update(bRecord.getRecordId(), bRecordFieldsToUpdate);
    }

    private void updateMatchInfo(List<ARecord> matchingARecords, List<BRecord> matchingBRecords, String ruleName) {
        for (ARecord aRecord : matchingARecords) {
            aRecord.getMatchMetadata().setStatus(Constants.MatchingStatus.Match.name());
            aRecord.getMatchMetadata().getMatchIds().addAll(matchingBRecords.stream().map((b -> b.getDisplayId())).collect(Collectors.toList()));
            aRecord.getMatchMetadata().setMatchRuleName(ruleName);

            ARecordDAO aRecordDAO = new ARecordDAO();
            Map<String, Object> aRecordFieldsToUpdate = new HashMap<>();
            aRecordFieldsToUpdate.put("matchMetadata.status", Constants.MatchingStatus.Match.name());
            aRecordFieldsToUpdate.put("matchMetadata.matchIds", aRecord.getMatchMetadata().getMatchIds());
            aRecordFieldsToUpdate.put("matchMetadata.matchRuleName", ruleName);
            aRecordDAO.update(aRecord.getRecordId(), aRecordFieldsToUpdate);
        }

        for (BRecord bRecord : matchingBRecords) {
            bRecord.getMatchMetadata().setStatus(Constants.MatchingStatus.Match.name());
            bRecord.getMatchMetadata().getMatchIds().addAll(matchingARecords.stream().map((a -> a.getDisplayId())).collect(Collectors.toList()));
            bRecord.getMatchMetadata().setMatchRuleName(ruleName);

            BRecordDAO bRecordDAO = new BRecordDAO();
            Map<String, Object> bRecordFieldsToUpdate = new HashMap<>();
            bRecordFieldsToUpdate.put("matchMetadata.status", Constants.MatchingStatus.Match.name());
            bRecordFieldsToUpdate.put("matchMetadata.matchIds", bRecord.getMatchMetadata().getMatchIds());
            bRecordFieldsToUpdate.put("matchMetadata.matchRuleName", ruleName);
            bRecordDAO.update(bRecord.getRecordId(), bRecordFieldsToUpdate);
        }
    }
}
