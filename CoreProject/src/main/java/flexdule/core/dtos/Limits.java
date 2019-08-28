package flexdule.core.dtos;

import java.io.Serializable;
import java.util.Objects;

import flexdule.core.util.Time;

// getters y setters directos de atributos, por referencia antinula ( es decir en caso de nulo la referencia es al hijo final)
// acceso directo a los hijos finales de los atributos, por referencia
// copyValues y copyRefs completos de su misma clase
public class Limits implements Serializable {

    private NX s;
    private NX f;

    public Limits() {
        cleanInit();
    }

    public Limits(Limits ls) {
        copyValues(ls);
    }

    public void cleanInit(){
        s = new NX();
        f = new NX();
    }

    public void copyValues(Limits ls){
        if(ls != null){
            s.copyValues(ls.getS());
            f.copyValues(ls.getF());
        }else{
            cleanInit();
        }
    }

    public void copyRefs(Limits ls){
        if(ls != null){
            s = ls.getS();
            f = ls.getF();
        }else{
            cleanInit();
        }
    }

    public NX getS() {
        return s;
    }

    public void setS(NX s) {
        this.s.copyRefs(s);
    }

    public NX getF() {
        return f;
    }

    public void setF(NX f) {
        this.f.copyRefs(f);
    }

    public void setSn(Time time) {
        this.s.setN(time);
    }

    public void setSx(Time time) {
        this.s.setX(time);
    }

    public void setFn(Time time) {
        this.f.setN(time);
    }

    public void setFx(Time time) {
        this.f.setX(time);
    }

    public Time getSn() {
        return this.s.getN();
    }

    public Time getSx() {
        return this.s.getX();
    }

    public Time getFn() {
        return this.f.getN();
    }

    public Time getFx() {
        return this.f.getX();
    }

    @Override
    public String toString() {
        return "Limits{" +
                "s=" + s +
                ", f=" + f +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Limits limits = (Limits) o;
        return Objects.equals(s, limits.s) &&
                Objects.equals(f, limits.f);
    }

    @Override
    public int hashCode() {
        return Objects.hash(s, f);
    }

}
