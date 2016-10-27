/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.web;

import com.ftt.elastic.match.beans.ARecord;
import com.ftt.elastic.match.beans.MatchConfig;
import com.ftt.elastic.match.db.ARecordDAO;
import com.ftt.elastic.match.db.MatchConfigDAO;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/index")
public class IndexingResource {

    @PUT
    @Path("/{matchName}/{sideName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMatchConfigs(String document, @PathParam("matchName") String matchName, @PathParam("sideName") String sideName) {

        // Get match config
        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();
        Map<String, Object> searchCriteria = new HashMap();
        searchCriteria.put("_id", matchName);
        List<MatchConfig> matchConfigs = matchConfigDAO.filter(searchCriteria, null, null);
        MatchConfig matchConfig = matchConfigs.get(0);

        //create record
        ARecordDAO aRecordDAO = new ARecordDAO();
        ARecord aRecord = new ARecord();
        aRecord.setMatchName(matchName);
        DBObject dBObject = (DBObject) JSON.parse(document);
        aRecord.setRecordId(UUID.randomUUID().toString());
        aRecord.setdBObject(dBObject);

        //compute key
        List<String> keys = matchConfig.getIdentifingKeys();
        String key = "";
        for (String k : keys) {
            Object value = aRecord.getdBObject().get(k);
            key+= ":"+String.valueOf(value);
        }
        aRecord.setKey(key);
        aRecord.setDisplayId((String) aRecord.getdBObject().get(matchConfig.getDisplayId()));

        aRecordDAO.create(aRecord);

        return Response.ok(aRecord).build();
    }
}
