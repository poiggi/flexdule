package com.flexdule.model;

import java.io.Serializable;
import java.time.Duration;

public class ActivityVars implements Serializable {

    private Duration in, ix;
    private Duration dn, dx;
    private Duration fn, fx;

    public ActivityVars() {
    }

    public Duration getIn() {
        return in;
    }

    public void setIn(Duration in) {
        this.in = in;
    }

    public Duration getIx() {
        return ix;
    }

    public void setIx(Duration ix) {
        this.ix = ix;
    }

    public Duration getDn() {
        return dn;
    }

    public void setDn(Duration dn) {
        this.dn = dn;
    }

    public Duration getDx() {
        return dx;
    }

    public void setDx(Duration dx) {
        this.dx = dx;
    }

    public Duration getFn() {
        return fn;
    }

    public void setFn(Duration fn) {
        this.fn = fn;
    }

    public Duration getFx() {
        return fx;
    }

    public void setFx(Duration fx) {
        this.fx = fx;
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
