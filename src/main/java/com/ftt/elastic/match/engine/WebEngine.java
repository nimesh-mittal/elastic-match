/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.engine;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.ftt.elastic.match.startup.StartupSettings;
import com.ftt.elastic.match.utils.Constants;
import com.ftt.elastic.match.utils.PropertiesRepo;
import com.ftt.elastic.match.utils.SystemHealthUtils;
import com.ftt.elastic.match.web.MatchConfigRestResource;
import com.ftt.elastic.match.web.MatchReportResource;
import com.ftt.elastic.match.web.SystemHealthResource;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Application;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.pmw.tinylog.Logger;

/**
 *
 * @author nimeshagarwal
 */
public class WebEngine {

    public void startWebServer() throws LifecycleException, InterruptedException, ServletException {
        StartupSettings.initEngine();
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(12273);
        tomcat.setBaseDir(PropertiesRepo.get(Constants.Settings.LOG_PATH) + "/working");

        Context context = tomcat.addContext("/", new File("./../ui/html/").getAbsolutePath());
        Logger.info("current working dir is {}", new File(PropertiesRepo.get(Constants.Settings.LOG_PATH) + "/working").getAbsoluteFile());

        tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
        Tomcat.addServlet(context, "welcome", new WelcomeServlet());
        Tomcat.addServlet(context, "hello", new HelloServlet());
        Tomcat.addServlet(context, "jersey-rest-servlet", resourceConfig());

        context.addServletMapping("/hello", "hello");
        context.addServletMapping("/rest/*", "jersey-rest-servlet");
        context.addServletMapping("/", "default");

        tomcat.start();
        SystemHealthUtils.create("Running", "web");
        tomcat.getServer().await();
    }

    public static class WelcomeServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            Writer writer = response.getWriter();
            writer.write("Hello, your webserver is up and running!!");
            writer.flush();
            Logger.info("request completed");
        }
    }

    public static class HelloServlet extends HttpServlet {

        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            Writer writer = response.getWriter();
            writer.write("Hello, welcome to elastic match");
            writer.flush();
            Logger.info("request completed");
        }
    }

    private ServletContainer resourceConfig() {
        return new ServletContainer(new ResourceConfig(
                new ResourceLoader().getClasses()));
    }

}

class ResourceLoader extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();

        // register root resource
        classes.add(MatchConfigRestResource.class);
        classes.add(MatchReportResource.class);
        classes.add(JacksonJsonProvider.class);
        classes.add(SystemHealthResource.class);
        return classes;
    }
}
