package com.flexdule.model.dtos;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

public class ActivityVars implements Serializable {

    private NX s;
    private NX d;
    private NX f;

    public ActivityVars() {
        s = new NX();
        d = new NX();
        f = new NX();
    }

    public NX getS() {
        return s;
    }

    public void setS(NX s) {
        if (s != null) {
            this.s = s;
        } else {
            this.s.setN(null);
            this.s.setX(null);
        }
    }

    public NX getD() {
        return d;
    }

    public void setD(NX d) {
        if (d != null) {
            this.d = d;
        } else {
            this.d.setN(null);
            this.d.setX(null);
        }
    }

    public NX getF() {
        return f;
    }

    public void setF(NX f) {
        if (f != null) {
            this.f = f;
        } else {
            this.f.setN(null);
            this.f.setX(null);
        }
    }

    public void setSn(Duration duration) {
        this.s.setN(duration);
    }

    public void setSx(Duration duration) {
        this.s.setX(duration);
    }

    public void setDn(Duration duration) {
        this.d.setN(duration);
    }

    public void setDx(Duration duration) {
        this.d.setX(duration);
    }

    public void setFn(Duration duration) {
        this.f.setN(duration);
    }

    public void setFx(Duration duration) {
        this.f.setX(duration);
    }

    public Duration getSn() {
        return this.s.getN();
    }

    public Duration getSx() {
        return this.s.getX();
    }

    public Duration getDn() {
        return this.d.getN();
    }

    public Duration getDx() {
        return this.d.getX();
    }

    public Duration getFn() {
        return this.f.getN();
    }

    public Duration getFx() {
        return this.f.getX();
    }

    @Override
    public String toString() {
        return "ActivityVars{" +
                "s=" + s +
                ", d=" + d +
                ", f=" + f +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityVars that = (ActivityVars) o;
        return Objects.equals(s, that.s) &&
                Objects.equals(d, that.d) &&
                Objects.equals(f, that.f);
    }

    @Override
    public int hashCode() {
        return Objects.hash(s, d, f);
    }
}
