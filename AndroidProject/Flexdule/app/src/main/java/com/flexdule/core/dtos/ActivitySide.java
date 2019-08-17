package com.flexdule.core.dtos;

import com.flexdule.core.util.CK;
import com.flexdule.core.util.Time;

import java.io.Serializable;

public class ActivitySide implements Serializable {

    public Time s, d, f;
    public Time limitS, limitF;

    public ActivitySide() {
    }

    public ActivitySide(Activity ac,  String side) {
        ActivityVars vars;
        Limits l = ac.getLimits();
        vars = ac.getConfigVars();

        switch (side){
            case CK.N:
                s = vars.getSn();
                d = vars.getDn();
                f = vars.getFn();
                limitS = l.getS().getN();
                limitF = l.getF().getN();
                break;
            case CK.X:
                s = vars.getSx();
                d = vars.getDx();
                f = vars.getFx();
                limitS = l.getS().getX();
                limitF = l.getF().getX();
                break;
            default: throw new IllegalArgumentException();
        }
    }
}
