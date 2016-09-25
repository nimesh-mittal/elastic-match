/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.test;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.MatchCondition;
import com.ftt.elastic.match.beans.MatchConfig;
import com.ftt.elastic.match.beans.MatchMetadata;
import com.ftt.elastic.match.beans.MatchRule;
import com.ftt.elastic.match.db.ARecordDAO;
import com.ftt.elastic.match.db.MatchConfigDAO;
import com.ftt.elastic.match.utils.Constants;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.testng.annotations.Test;

/**
 *
 * @author nimeshagarwal
 */
public class ConfigDaoTest {

    //@Test
    public void testMethod() {
        MatchConfig matchConfig = new MatchConfig();
        matchConfig.setName("person-match");
        matchConfig.setSideAName("a-person");
        matchConfig.setSideBName("b-person");
        matchConfig.setPrimarySide("SideA");
        matchConfig.setDisplayId("id");
        matchConfig.getIdentifingKeys().add("profession");

        MatchRule matchRule1 = new MatchRule();
        matchRule1.setName("rule1");
        matchRule1.setPriority("1");
        matchRule1.setMatchType(Constants.MatchingTypes.OneToOne.name());
        matchRule1.getKeys().add("profession");

        MatchCondition matchCondition1 = new MatchCondition("color", "==", "0");
        matchRule1.getConditions().add(matchCondition1);

        matchConfig.getMatchingRules().add(matchRule1);

        MatchRule matchRule2 = new MatchRule();
        matchRule2.setName("rule2");
        matchRule2.setPriority("2");
        matchRule2.setMatchType(Constants.MatchingTypes.OneToOne.name());
        matchRule2.getKeys().add("profession");

        MatchCondition matchCondition2 = new MatchCondition("salary", "=", "1");
        matchRule2.getConditions().add(matchCondition2);

        matchConfig.getMatchingRules().add(matchRule2);

        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
        matchConfigDAO.create(matchConfig);
    }

    //@Test
    public void testPersonMatch() {
        MatchConfig matchConfig = new MatchConfig();
        matchConfig.setName("compatibility-match");
        matchConfig.setSideAName("a-person");
        matchConfig.setSideBName("b-person");
        matchConfig.setSideADirectoryPath("/opt/dev/data/sideA/");
        matchConfig.setSideBDirectoryPath("/opt/dev/data/sideB/");
        matchConfig.setPrimarySide("SideA");
        matchConfig.setDisplayId("id");
        matchConfig.getIdentifingKeys().add("city");

        MatchRule matchRule1 = new MatchRule();
        matchRule1.setName("rule1");
        matchRule1.setPriority("1");
        matchRule1.setMatchType(Constants.MatchingTypes.OneToOne.name());
        matchRule1.getKeys().add("profession");

        MatchCondition matchCondition1 = new MatchCondition("salary", ">=", "1000");
        MatchCondition matchCondition4 = new MatchCondition("gender", "!=", "0");
        matchRule1.getConditions().add(matchCondition1);
        matchRule1.getConditions().add(matchCondition4);

        matchConfig.getMatchingRules().add(matchRule1);

        MatchRule matchRule2 = new MatchRule();
        matchRule2.setName("rule2");
        matchRule2.setPriority("2");
        matchRule2.setMatchType(Constants.MatchingTypes.OneToOne.name());
        matchRule2.getKeys().add("degree");

        MatchCondition matchCondition2 = new MatchCondition("age", ">=", "1");
        MatchCondition matchCondition3 = new MatchCondition("gender", "!=", "0");
        matchRule2.getConditions().add(matchCondition2);
        matchRule2.getConditions().add(matchCondition3);

        matchConfig.getMatchingRules().add(matchRule2);

        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
        matchConfigDAO.create(matchConfig);
    }

    //@Test
    public void testPerson2Match() {
        MatchConfig matchConfig = new MatchConfig();
        matchConfig.setName("people-match");
        matchConfig.setSideAName("students");
        matchConfig.setSideBName("teachers");
        matchConfig.setSideADirectoryPath("/opt/dev/data/sideA/");
        matchConfig.setSideBDirectoryPath("/opt/dev/data/sideB/");
        matchConfig.setPrimarySide("SideA");
        matchConfig.setDisplayId("studentId");
        matchConfig.getIdentifingKeys().add("city");
        matchConfig.getIdentifingKeys().add("rank");

        MatchRule matchRule1 = new MatchRule();
        matchRule1.setName("rule1");
        matchRule1.setPriority("1");
        matchRule1.setMatchType(Constants.MatchingTypes.OneToOne.name());
        matchRule1.getKeys().add("major");
        matchRule1.getKeys().add("college");

        MatchCondition matchCondition1 = new MatchCondition("age", "<=", "0");
        MatchCondition matchCondition4 = new MatchCondition("gender", "!=", "0");
        matchRule1.getConditions().add(matchCondition1);
        matchRule1.getConditions().add(matchCondition4);

        matchConfig.getMatchingRules().add(matchRule1);

        MatchRule matchRule2 = new MatchRule();
        matchRule2.setName("rule2");
        matchRule2.setPriority("2");
        matchRule2.setMatchType(Constants.MatchingTypes.OneToOne.name());
        matchRule2.getKeys().add("degree");

        MatchCondition matchCondition2 = new MatchCondition("fee", ">=", "1");
        MatchCondition matchCondition3 = new MatchCondition("gender", "!=", "0");
        matchRule2.getConditions().add(matchCondition2);
        matchRule2.getConditions().add(matchCondition3);

        matchConfig.getMatchingRules().add(matchRule2);

        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
        matchConfigDAO.create(matchConfig);
    }
    
    //@Test
    public void readTest() {
        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
        MatchConfig matchConfig1 = matchConfigDAO.get("compatibility-match");
        System.out.println("=>" + matchConfig1.getPrimarySide());
    }

    //@Test
    public void recordTest() {
        ARecordDAO aRecordDAO = new ARecordDAO();

        ARecord arecord = new ARecord();
        arecord.setDisplayId("A102");
        arecord.setMatchMetadata(new MatchMetadata());
        arecord.getMatchMetadata().setStatus("UN_MATCHED");

        String json = "{items: [{\"name\":\"orange\", \"unit\":10}, {\"name\":\"apple\", \"unit\":2}]}";
        DBObject dbObject = (DBObject) JSON.parse(json);
        arecord.setdBObject(dbObject);
        aRecordDAO.create(arecord);
    }
}
