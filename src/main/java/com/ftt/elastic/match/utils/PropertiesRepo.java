/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.pmw.tinylog.Logger;

/**
 *
 * @author nimeshagarwal
 */
public class PropertiesRepo {

    private static final Properties prop = new Properties();

    public static void load() {
        InputStream input = null;

        try {
            input = new FileInputStream("./../config/config.properties");

            // load a properties file
            prop.load(input);
            
            //print loaded properties
            Logger.info("listing configurations ...");
            for(String proKey: prop.stringPropertyNames())
            {
                 Logger.info(proKey + " --> " + prop.getProperty(proKey));
            }
             Logger.info("listing configurations completed");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String get(String name) {
        return prop.getProperty(name);
    }

    public static Integer getInt(String name) {
        return Integer.parseInt(prop.getProperty(name));
    }
}
