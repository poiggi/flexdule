package flexdule.core.dtos;

import java.io.Serializable;
import java.util.Objects;

import flexdule.core.util.Time;

public class ActivityVars implements Serializable {
	private static final long serialVersionUID = 5648352595211361825L;

	private NX s;
	private NX d;
	private NX f;

	public ActivityVars() {
		cleanInit();
	}

	public ActivityVars(ActivityVars av) {
		cleanInit();
		copyValues(av);
	}

	public void cleanInit() {
		s = new NX();
		d = new NX();
		f = new NX();
	}

	public void copyValues(ActivityVars av) {
		if (av != null) {
			s.copyValues(av.getS());
			d.copyValues(av.getD());
			f.copyValues(av.getF());
		} else {
			cleanInit();
		}
	}

	public void copyRefs(ActivityVars av) {
		if (av != null) {
			s = av.getS();
			d = av.getD();
			f = av.getF();
		} else {
			cleanInit();
		}
	}

	public NX getS() {
		return s;
	}

	public void setS(NX s) {
		this.s.copyRefs(s);
	}

	public NX getD() {
		return d;
	}

	public void setD(NX d) {
		this.d.copyRefs(d);
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

	public void setDn(Time time) {
		this.d.setN(time);
	}

	public void setDx(Time time) {
		this.d.setX(time);
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

	public Time getDn() {
		return this.d.getN();
	}

	public Time getDx() {
		return this.d.getX();
	}

	public Time getFn() {
		return this.f.getN();
	}

	public Time getFx() {
		return this.f.getX();
	}

	@Override
	public String toString() {
		return "ActivityVars{ " + "Sn=" + getSn() + ", Sx=" + getSx() + ", " + "Dn=" + getDn() + ", Dx=" + getDx()
				+ ", " + "Fn=" + getFn() + ", Fx=" + getFx() + " }";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ActivityVars that = (ActivityVars) o;
		return Objects.equals(s, that.s) && Objects.equals(d, that.d) && Objects.equals(f, that.f);
	}

	@Override
	public int hashCode() {
		return Objects.hash(s, d, f);
	}

}
