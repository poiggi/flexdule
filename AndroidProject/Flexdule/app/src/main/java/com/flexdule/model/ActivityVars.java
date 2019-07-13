package com.flexdule.model;

import com.flexdule.util.U;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;

public class ActivityVars implements Serializable {

    private Duration in, ix;
    private Duration dn, dx;
    private Duration fn, fx;

    public ActivityVars(){
    }


    @Override
    public String toString() {
        return "ActivityVars{" +
                ", ix=" + ix +
                ", dn=" + dn +
                ", dx=" + dx +
                ", fn=" + fn +
                ", fx=" + fx +
                '}';
    }
}
