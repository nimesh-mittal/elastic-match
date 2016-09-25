/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.web;

import com.ftt.elastic.match.beans.SystemHealth;
import com.ftt.elastic.match.db.SystemHealthDAO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/system-health")
public class SystemHealthResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response report() {
        SystemHealthDAO systemHealthDAO = new SystemHealthDAO();

        SystemHealth systemHealth = systemHealthDAO.get();

        return Response.ok(systemHealth).build();
    }
}
