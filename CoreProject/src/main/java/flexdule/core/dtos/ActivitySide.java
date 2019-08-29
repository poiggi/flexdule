package flexdule.core.dtos;

import java.io.Serializable;

import flexdule.core.util.K;
import flexdule.core.util.Time;

public class ActivitySide implements Serializable {
	private static final long serialVersionUID = -5142660684828226213L;
	
	public Time s, d, f;
    public Time limitS, limitF;

    public ActivitySide() {
    }

    public ActivitySide(Activity ac,  String side) {
        ActivityVars vars;
        Limits l = ac.getLimits();
        vars = ac.getConfigVars();

        switch (side){
            case K.N:
                s = vars.getSn();
                d = vars.getDn();
                f = vars.getFn();
                limitS = l.getS().getN();
                limitF = l.getF().getN();
                break;
            case K.X:
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
