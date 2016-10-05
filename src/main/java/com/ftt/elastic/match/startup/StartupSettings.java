/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.startup;

import com.ftt.elastic.match.utils.PropertiesRepo;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Level;
import org.pmw.tinylog.labelers.CountLabeler;
import org.pmw.tinylog.policies.SizePolicy;
import org.pmw.tinylog.writers.ConsoleWriter;
import org.pmw.tinylog.writers.RollingFileWriter;

/**
 *
 * @author nimeshagarwal
 */
public class StartupSettings {

    public StartupSettings() {
    }

    public static void initEngine() {
        setupEngineLogConfig();
    }

    public static void initBatch() {
        setupBatchLogConfig();
    }

    private static void setupBatchLogConfig() {
        Configurator.defaultConfig()
                .writer(new RollingFileWriter(PropertiesRepo.get("log.path") + "/data-import.log", 10, new CountLabeler(), new SizePolicy(1024 * 1024 * 100)))
                .addWriter(new ConsoleWriter())
                .level(Level.valueOf(PropertiesRepo.get("log.level")))
                .formatPattern(PropertiesRepo.get("log.pattern"))
                .activate();
    }

    private static void setupEngineLogConfig() {
        Configurator.defaultConfig()
                .writer(new RollingFileWriter(PropertiesRepo.get("log.path") + "/matching-engine.log", 10, new CountLabeler(), new SizePolicy(1024 * 1024 * 100)))
                .addWriter(new ConsoleWriter())
                .level(Level.valueOf(PropertiesRepo.get("log.level")))
                .formatPattern(PropertiesRepo.get("log.pattern"))
                .activate();
    }
}
