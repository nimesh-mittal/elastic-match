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

/**
 *
 * @author nimeshagarwal
 */
public class PropertiesRepo {

    private static Properties prop = new Properties();

    public static void load() {
        InputStream input = null;

        try {
            input = new FileInputStream("/opt/dev/apps/elastic/config/config.properties");

            // load a properties file
            prop.load(input);
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
