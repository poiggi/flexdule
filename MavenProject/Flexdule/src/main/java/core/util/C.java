package core.util;

import java.util.Random;

/**
 * Clase de útils pertenecientes al Core
 */
public class C {


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
            case K.S:
                varN = K.SN;
                break;
            case K.D:
                varN = K.DN;
                break;
            case K.F:
                varN = K.FN;
                break;
            default:
                throw new IllegalArgumentException("Invalid var argument.");
        }
        return varN;
    }

    public static String getXTag(String var){
        String varX;
        switch (var) {
            case K.S:
                varX = K.SX;
                break;
            case K.D:
                varX = K.DX;
                break;
            case K.F:
                varX = K.FX;
                break;
            default:
                throw new IllegalArgumentException("Invalid var argument.");
        }
        return varX;
    }
}
