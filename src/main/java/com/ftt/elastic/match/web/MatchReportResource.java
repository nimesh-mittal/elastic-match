/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.web;

import com.ftt.elastic.match.beans.Report;
import com.ftt.elastic.match.db.ReportDAO;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/matching-reports")
public class MatchReportResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response report() {
        ReportDAO reportDAO = new ReportDAO();

        List<Report> report = reportDAO.report();

        return Response.ok(report).build();
    }
}
