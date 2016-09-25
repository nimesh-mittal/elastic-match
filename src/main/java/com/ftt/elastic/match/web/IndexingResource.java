/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.web;

import com.ftt.elastic.match.beans.MatchConfig;
import com.ftt.elastic.match.db.MatchConfigDAO;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.pmw.tinylog.Logger;

@Path("/index")
public class IndexingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listMatchConfigs() {
        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();

        Map<String, Object> searchCriteria = new HashMap();
        List<MatchConfig> matchConfigs = matchConfigDAO.filter(searchCriteria, null, null);

        return Response.ok(matchConfigs).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMatchConfigs(MatchConfig config) {
        MatchConfigDAO matchConfigDAO = new MatchConfigDAO();

        String error = validateAndCreateDirectories(config);
        if (error != null) {
            String errorMessage = "{";
            errorMessage += "\"status\":\"error\",";
            errorMessage += "\"message\":\"" + error + "\"";
            errorMessage += "}";
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
        }

        String id = matchConfigDAO.create(config);

        return Response.ok("{ \"id\":\""+id+"\"}").build();
    }

    private String validateAndCreateDirectories(MatchConfig config) {
        if (config.getSideADirectoryPath() != null && config.getSideBDirectoryPath() != null) {
            File sideA = new File(config.getSideADirectoryPath());
            Boolean sideAValid = sideA.exists() && sideA.isDirectory() && sideA.canWrite() && sideA.canRead();

            File sideB = new File(config.getSideBDirectoryPath());
            Boolean sideBValid = sideB.exists() && sideB.isDirectory() && sideB.canWrite() && sideB.canRead();

            Boolean isDifferent = !config.getSideADirectoryPath().equalsIgnoreCase(config.getSideBDirectoryPath());
            if (sideAValid && sideBValid && isDifferent) {
                File workingDir = new File(sideA.getAbsoluteFile() + "/../working");
                Logger.info("creating working directory at {} ", workingDir.getAbsolutePath());
                workingDir.mkdir();
                File archiveDir = new File(sideA.getAbsoluteFile() + "/../archive");
                Logger.info("creating archive directory at {} ", archiveDir.getAbsolutePath());
                archiveDir.mkdir();
                return null;
            } else {
                return "sideADirectoryPath or sideBDirectoryPath is not valid. Ensure directory exist and has read and write permissions. Ensure both sides are pointing to different directory.";
            }
        } else {
            return "sideADirectoryPath or sideBDirectoryPath is null";
        }
    }
}
