package flexdule.core.dtos;

import java.io.Serializable;
import java.util.Objects;

import flexdule.core.util.Time;

// getters y setters directos de atributos, por referencia, nulable
// copyValues y copyRefs completos de su misma clase
public class NX implements Serializable {

    private Time n;
    private Time x;

    public NX() {
    }

    public NX(NX nx) {
        copyValues(nx);
    }

    public void copyValues(NX nx) {
        if (nx != null) {
            this.n.copyValue(nx.getN());
            this.x.copyValue(nx.getX());
        } else {
            this.n = null;
            this.x = null;
        }
    }

    public void copyRefs(NX nx) {
        if (nx != null) {
            n = nx.getN();
            x = nx.getX();
        } else {
            this.n = null;
            this.x = null;
        }
    }

    public Time getN() {
        return n;
    }

    public void setN(Time n) {
        this.n = n;
    }

    public Time getX() {
        return x;
    }

    public void setX(Time x) {
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
