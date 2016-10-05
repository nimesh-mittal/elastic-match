/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.utils;

import java.util.Date;
import org.pmw.tinylog.Logger;

/**
 *
 * @author nimeshagarwal
 */
public class LogUtils {

    public static void logStart() {
        Logger.info("============= Start [{}] ==============", new Date());
    }

    public static void logEnd() {
        Logger.info("========================= == End == ==============================", new Date());
    }
}
