/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftt.elastic.match.test;

import org.apache.commons.lang3.math.NumberUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author nimeshagarwal
 */
public class GeneralTest {

    @Test
    public void test1() {
        Assert.assertEquals(NumberUtils.isNumber("1.0"), true);
        Assert.assertEquals(NumberUtils.isNumber("1"), true);
        Assert.assertEquals(NumberUtils.isNumber("-127272.07266335"), true);
        Assert.assertEquals(NumberUtils.isNumber("-127272.07s"), false);
    }
}
