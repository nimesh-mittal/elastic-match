/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.utils;

import com.ftt.elastic.match.beans.SystemHealth;
import com.ftt.elastic.match.db.SystemHealthDAO;
import java.util.Date;

/**
 *
 * @author nimeshagarwal
 */
public class SystemHealthUtils {
    
    public static void create(String status, String type) {
        SystemHealthDAO systemHealthDAO = new SystemHealthDAO();
        SystemHealth health = systemHealthDAO.get();
        
        if(health == null){
            health = new SystemHealth();
        }
        
        if ("web".equalsIgnoreCase(type)) {
            health.setWebEngine(status);
        } else if ("import".equalsIgnoreCase(type)) {
            health.setImportEngine(status);
        } else if ("matching".equalsIgnoreCase(type)) {
            health.setMatchingEngine(status);
        }
        
        health.setLastRecorded(new Date());
        systemHealthDAO.create(health);
    }
}
