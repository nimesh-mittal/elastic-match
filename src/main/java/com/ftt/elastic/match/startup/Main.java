/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.startup;

import com.ftt.elastic.match.engine.FileWatcher;
import com.ftt.elastic.match.engine.MatchingEngine;
import com.ftt.elastic.match.exception.BadConfigException;
import com.ftt.elastic.match.utils.PropertiesRepo;
import com.ftt.elastic.match.utils.SystemHealthUtils;
import com.ftt.elastic.match.engine.WebEngine;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;

/**
 *
 * @author nimeshagarwal
 */
public class Main {

    public static void main(String[] args) throws MalformedURLException {

        PropertiesRepo.load();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // schedule file watcher
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FileWatcher fileWatcher = new FileWatcher();
                    fileWatcher.start();
                } catch (BadConfigException | InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    SystemHealthUtils.create("Failed", "import");
                }
            }
        });

        // schedule matching engine
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    MatchingEngine matchingEngine = new MatchingEngine();
                    matchingEngine.start();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    SystemHealthUtils.create("Failed", "matching");
                }
            }
        });

        // schedule web server
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    WebEngine webEngine = new WebEngine();
                    webEngine.startWebServer();
                    SystemHealthUtils.create("Running", "web");
                } catch (LifecycleException | ServletException | InterruptedException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    SystemHealthUtils.create("Failed", "web");
                }
            }
        });

    }
}
