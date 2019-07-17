package com.flexdule.model.dtos;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;

public class NX implements Serializable {

    private Duration n;
    private Duration x;

    public Duration getN() {
        return n;
    }

    public void setN(Duration n) {
        this.n = n;
    }

    public Duration getX() {
        return x;
    }

    public void setX(Duration x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "NX{" +
                "n=" + n +
                ", x=" + x +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NX nx = (NX) o;
        return Objects.equals(n, nx.n) &&
                Objects.equals(x, nx.x);
    }

    @Override
    public int hashCode() {
        return Objects.hash(n, x);
    }
}
