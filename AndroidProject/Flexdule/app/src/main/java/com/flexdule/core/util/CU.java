package com.flexdule.core.util;

import java.time.Duration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase de útils pertenecientes al Core
 */
public class CU {


    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String getNTag(String var){
        String varN;
        switch (var) {
            case CK.S:
                varN = CK.SN;
                break;
            case CK.D:
                varN = CK.DN;
                break;
            case CK.F:
                varN = CK.FN;
                break;
            default:
                throw new IllegalArgumentException("Invalid var argument.");
        }
        return varN;
    }

    public static String getXTag(String var){
        String varX;
        switch (var) {
            case CK.S:
                varX = CK.SX;
                break;
            case CK.D:
                varX = CK.DX;
                break;
            case CK.F:
                varX = CK.FX;
                break;
            default:
                throw new IllegalArgumentException("Invalid var argument.");
        }
        return varX;
    }
}
