package com.flexdule.core.manager;

import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.ActivityVars;
import com.flexdule.core.dtos.Limits;
import com.flexdule.core.dtos.NX;
import com.flexdule.core.util.CoreLog;
import com.flexdule.core.util.K;
import com.flexdule.core.util.Time;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivitiesManager {
    private CoreLog log;


    private Time scheStart = new Time("00:00");
    private Time scheFin = new Time("23:59");

    public ScheduleActivitiesManager(CoreLog log) {
        this.log = log;
    }

    public void calcContext(List<Activity> acs) throws Exception {
        log.i("BEGIN calcContext()");
        log.d("calcContext() activities = " + acs);

        try {

            // Se extraen las variables finales de las de configuración
            for (Activity ac : acs) {
                calcFinalFromConfig(ac);
            }

            boolean found;
            int c = 0;
            // Se calculan las variables internas que se puedan derivar de las existentes,
            // así como los límites que dependen de ellas
            do {
                boolean inner = calcActivitiesInnerTimes(acs);
                boolean limits = calcLimits(acs);
                found = inner || limits;

                c++;
                log.i("Calc inner & limits. c= " + c + ", found= " + found);
            } while (found);

            // Por separado, para no afectar a los límites calculados, se calculan las variables
            // internas teniendo en cuenta los límites calculados previamente
            c = 0;
            do {
                boolean limits = calcInnerWithLimits(acs);
                boolean inner = calcActivitiesInnerTimes(acs);
                found = inner || limits;

                c++;
                log.i("Calc inner & innerWithLimtis. c= " + c + ", found= " + found);
            } while (found);

        } catch (Exception e) {
            log.e("Error in calcContext(): " + e);
            throw e;
        }

        log.d("calcContext() activities:");
        for (Activity ac : acs) {
            log.d(ac.toString());
        }
        log.i("END calcContext()");
    }

    public void calcFinalFromConfig(Activity ac) throws Exception {
        log.d("BEGIN copyConfigToFinal(). ac= " + ac);
        try {

            ActivityVars conf = ac.getConfigVars();
            ActivityVars fin = ac.getFinalVars();

            //Sn
            fin.setSn(Time.copy(conf.getSn()));

            //Sx
            if (conf.getSx() != null) {
                boolean validSx = true;
                if (fin.getSn() != null && conf.getSx().lessThan(fin.getSn())) {
                    log.w("Sx < Sn !");
                    validSx = false;
                }
                if (validSx) fin.setSx(Time.copy(conf.getSx()));
            }

            //Dn
            fin.setDn(Time.copy(conf.getDn()));

            //Dx
            if (conf.getDx() != null) {
                boolean validDx = true;
                if (fin.getDn() != null && conf.getDx().lessThan(fin.getDn())) {
                    log.w("Dx < Dn !");
                    validDx = false;
                }
                if (validDx) fin.setDx(Time.copy(conf.getDx()));
            }

            //Fn
            if (conf.getFn() != null) {
                boolean validFn = true;
                if (fin.getSn() != null && conf.getFn().lessThan(fin.getSn())) {
                    log.w("Fn < Sn !");
                    validFn = false;
                }
                if (fin.getSx() != null && conf.getFn().lessThan(fin.getSx())) {
                    log.w("Fn < Sx !");
                    validFn = false;
                }
                if (validFn) fin.setFn(Time.copy(conf.getFn()));
            }

            //Fx
            if (conf.getFx() != null) {
                boolean validFx = true;
                if (fin.getSn() != null && conf.getFx().lessThan(fin.getSn())) {
                    log.w("Fx < Sn !");
                    validFx = false;
                }
                if (fin.getSx() != null && conf.getFx().lessThan(fin.getSx())) {
                    log.w("Fx < Sx !");
                    validFx = false;
                }
                if (validFx) fin.setFx(Time.copy(conf.getFx()));
            }

        } catch (Exception e) {
            log.e("Error in :" + e);
            throw e;
        }
        log.d("END copyConfigToFinal(). ac= " + ac);
    }

    public boolean calcActivitiesInnerTimes(List<Activity> acs) throws Exception {
        boolean found = false;
        log.i("BEGIN calcActivitiesInnerTimes()");
        try {
            for (int i = 0; i < acs.size(); i++) {
                boolean foundI = calcActivityInnerTimes(acs.get(i));
                found = found || foundI;
            }
        } catch (Exception e) {
            log.e("Error in calcActivitiesInnerTimes(): " + e);
            throw e;
        }
        log.i("END calcActivitiesInnerTimes(). found= " + found);
        return found;
    }

    public boolean calcActivityInnerTimes(Activity activity) {
        log.i("BEGIN calcActivityInnerTimes(). activity= " + activity);
        boolean found = false;
        try {
            ActivityVars v = activity.getFinalVars();
            Time foundSn = calcSn(activity.getFinalVars());
            if (foundSn != null) {
                v.setSn(foundSn);
                found = true;
            }
            Time foundDn = calcDn(activity.getFinalVars());
            if (foundDn != null) {
                v.setDn(foundDn);
                found = true;
            }
            Time foundFn = calcFn(activity.getFinalVars());
            if (foundFn != null) {
                v.setFn(foundFn);
                found = true;
            }
            Time foundSx = calcSx(activity.getFinalVars());
            if (foundSx != null) {
                v.setSx(foundSx);
                found = true;
            }
            Time foundDx = calcDx(activity.getFinalVars());
            if (foundDx != null) {
                v.setDx(foundDx);
                found = true;
            }
            Time foundFx = calcFx(activity.getFinalVars());
            if (foundFx != null) {
                v.setFx(foundFx);
                found = true;
            }
        } catch (Exception e) {
            log.e("Error in calcActivityInnerTimes(): " + e);
            throw e;
        }
        log.i("END calcActivityInnerTimes(). activity= " + activity);
        return found;
    }

    public boolean calcLimits(List<Activity> acs) throws Exception {
        log.i("BEGIN calcLimits()");
        boolean found = false;
        try {
            for (int i = 0; i < acs.size(); i++) {
                Limits l = acs.get(i).getLimits();

                // Límites iniciales
                Time foundLSn = calcLimitSn(acs, i);
                if (foundLSn != null) {
                    l.setSn(foundLSn);
                    found = true;
                }
                Time foundLSx = calcLimitSx(acs, i);
                if (foundLSx != null) {
                    l.setSx(foundLSx);
                    found = true;
                }

                // Límites finales
                Time foundLFn = calcLimitFn(acs, i);
                if (foundLFn != null) {
                    l.setFn(foundLFn);
                    found = true;
                }
                Time foundLFx = calcLimitFx(acs, i);
                if (foundLFx != null) {
                    l.setFx(foundLFx);
                    found = true;
                }
            }
        } catch (Exception e) {
            log.e("Error in calcLimits(): " + e);
            throw e;
        }
        log.i("END calcLimits(). found= " + found);
        return found;
    }

    public boolean calcInnerWithLimits(List<Activity> acs) {
        log.i("BEGIN calcInnerWithLimits().");
        boolean found = false;
        try {

            for (Activity ac : acs) {
                ActivityVars v = ac.getFinalVars();
                Limits l = ac.getLimits();

                Time foundSn = calcSnWithLimit(v.getSn(), l.getSn());
                if (foundSn != null) {
                    v.setSn(foundSn);
                    found = true;
                }

                Time foundFx = calcFxWithLimit(v.getFx(), l.getFx());
                if (foundFx != null) {
                    found = true;
                    v.setFx(foundFx);
                }
            }
        } catch (Exception e) {
            log.e("Error in calcInnerWithLimits(): " + e);
            throw e;
        }
        log.i("END calcInnerTimeWithLimits(). found= " + found);
        return found;
    }

    public Time calcSn(ActivityVars v) {
        Time sn = null;
        if (v.getSn() == null && v.getFx() != null && v.getDx() != null) {
            sn = Time.sub(v.getFx(), v.getDx());
            log.i("found Sn= " + sn + "= (Fx)" + v.getFx() + " - (Dx)" + v.getDx());
        }
        return sn;
    }

    public Time calcSx(ActivityVars v) {
        Time sx = null;
        if (v.getSx() == null && v.getFn() != null && v.getDn() != null) {
            sx = Time.sub(v.getFn(), v.getDn());
            log.i("found Sx= " + sx + "= (Fn)" + v.getFn() + " - (Dn)" + v.getDn());
        }
        return sx;
    }

    public Time calcDn(ActivityVars v) {
        Time dn = null;
        if (v.getDn() == null && v.getFn() != null && v.getSx() != null) {
            dn = Time.sub(v.getFn(), v.getSx());
            log.i("found Dn= " + dn + "= (Fn)" + v.getFn() + " - (Sx)" + v.getSx());
        }
        return dn;
    }

    public Time calcDx(ActivityVars v) {
        Time dx = null;
        if (v.getDx() == null && v.getFx() != null && v.getSn() != null) {
            dx = Time.sub(v.getFx(), v.getSn());
            log.i("found Dx= " + dx + "= (Fx)" + v.getFx() + " - (Sn)" + v.getSn());
        }
        return dx;
    }

    public Time calcFn(ActivityVars v) {
        Time fn = null;
        if (v.getFn() == null && v.getSx() != null && v.getDn() != null) {
            fn = Time.sum(v.getSx(), v.getDn());
            log.i("found Fn= " + fn + "= (Sx)" + v.getSx() + " - (Dn)" + v.getDn());
        }
        return fn;
    }

    public Time calcFx(ActivityVars v) {
        Time fx = null;
        if (v.getFx() == null && v.getSn() != null && v.getDx() != null) {
            fx = Time.sum(v.getSn(), v.getDx());
            log.i("found Fx= " + fx + "= (Sn)" + v.getSn() + " - (Dx)" + v.getDx());
        }
        return fx;
    }

    public Time calcSnWithLimit(Time sn, Time limitSn) {
        Time found = null;
        if (sn == null && limitSn != null) {
            found = Time.copy(limitSn);
            log.i("found Sn= " + found + "= (limitSn)" + limitSn);
        }
        return found;
    }

    public Time calcFxWithLimit(Time fx, Time limitFx) {
        Time found = null;
        if (fx == null && limitFx != null) {
            found = Time.copy(limitFx);
            log.i("found Fx= " + found + "= (limitFx)" + limitFx);
        }
        return found;
    }


    /* GUIÓN de cálculo MinMax

Para seleccionar, tener en cuenta la posible convinación de valores existentes, incluyendo
AUSENCIAS del lado tratado.

Determinantes
Límite vecino

Gemela lado contrario limitante (FLEXIBLE solo)
    Calculada
Límite vecino lado contrario

Variable opuesta
Limite opuesto

Variable opuesta lado contrario
Límite opuesto lado contrario

Otra variable afectante
    (D?)
        con final
        con limite

     */

    // Los límites no se afectan entre lados ( N <-> X ). Esas limitaciones son del ámbito del
    // minMax.
    // Porque los límites son factores puramente contextuales, y las variables internas pueden
    // cambiar.
    public Time calcLimitSn(List<Activity> acs, int objectIndex) {
        Activity obj = acs.get(objectIndex);
        log.i("BEGIN calcLimitSn(). objectIndex = " + objectIndex + " ( " + obj.getName() + " )");
        Time existing = obj.getLimits().getSn();
        int i = objectIndex - 1;
        Time found = null;
        try {

            while (existing == null && found == null && i >= 0) {
                Activity ac = acs.get(i);
                log.d("finding Sn from index= " + i + ", activity= " + ac);
                ActivityVars v = ac.getFinalVars();

                if (v.getFn() != null) {
                    found = Time.copy(v.getFn());
                }
                if (found == null && v.getSn() != null) {
                    found = Time.copy(v.getSn());
                }

                i--;
            }
        } catch (Exception e) {
            log.e("Error in calcLimitSn():" + e);
            throw e;
        }
        log.i("END calcLimitSn(). existing = " + existing + ", found= " + found);
        return found;
    }

    public Time calcLimitSx(List<Activity> acs, int objectIndex) {
        Activity obj = acs.get(objectIndex);
        log.i("BEGIN calcLimitSx(). objectIndex = " + objectIndex + " ( " + obj.getName() + " )");
        Time existing = obj.getLimits().getSx();
        int i = objectIndex - 1;
        Time found = null;
        try {

            while (existing == null && found == null && i >= 0) {
                Activity ac = acs.get(i);
                log.d("finding Sx from index= " + i + ", activity= " + ac);
                ActivityVars v = ac.getFinalVars();

                if (v.getFx() != null) {
                    found = Time.copy(v.getFx());
                }
                if (found == null && v.getSx() != null) {
                    found = Time.copy(v.getSx());
                }
                i--;
            }
        } catch (Exception e) {
            log.e("Error in calcLimitSx():" + e);
            throw e;
        }
        log.i("END calcLimitSx(). existing = " + existing + ", found= " + found);
        return found;
    }

    public Time calcLimitFn(List<Activity> acs, int objectIndex) {
        Activity obj = acs.get(objectIndex);
        log.i("BEGIN calcLimitFn(). objectIndex = " + objectIndex + " ( " + obj.getName() + " )");
        Time existing = obj.getLimits().getFn();
        int i = objectIndex + 1;
        Time found = null;
        try {

            while (existing == null && found == null && i < acs.size()) {
                Activity ac = acs.get(i);
                log.d("finding Fn from index= " + i + ", activity= " + ac);
                ActivityVars v = ac.getFinalVars();

                if (v.getSn() != null) {
                    found = Time.copy(v.getSn());
                }
                if (found == null && v.getFn() != null) {
                    found = Time.copy(v.getFn());
                }
                i++;
            }
        } catch (Exception e) {
            log.e("Error in calcLimitFn():" + e);
            throw e;
        }
        log.i("END calcLimitFn(). existing = " + existing + ", found= " + found);
        return found;
    }

    public Time calcLimitFx(List<Activity> acs, int objectIndex) {
        Activity obj = acs.get(objectIndex);
        log.i("BEGIN calcLimitFx(). objectIndex = " + objectIndex + " ( " + obj.getName() + " )");
        Time existing = obj.getLimits().getFx();
        int i = objectIndex + 1;
        Time found = null;
        try {

            while (existing == null && found == null && i < acs.size()) {
                Activity ac = acs.get(i);
                log.d("finding Fx from index= " + i + ", activity= " + ac);
                ActivityVars v = ac.getFinalVars();

                if (v.getSx() != null) {
                    found = Time.copy(v.getSx());
                }
                if (found == null && v.getFx() != null) {
                    found = Time.copy(v.getFx());
                }
                i++;
            }
        } catch (Exception e) {
            log.e("Error in calcLimitFx():" + e);
            throw e;
        }
        log.i("END calcLimitFx(). existing = " + existing + ", found= " + found);
        return found;
    }

    public NX calcMinMaxSn(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxSn(). activity= " + ac + ", isFlexible= " + isFlexible);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n = null, x;

        addScheduleMargins(lim);

        // limit S
        log.d("Findind Min...");
        if (v.getDx() != null && v.getFx() != null) { // Determinantes
            n = Time.sub(v.getFx(), v.getDx());
            log.d("found Sn min= " + n + "= (Fx)" + v.getFx() + " - (Dx)" + v.getDx());
        } else if (lim.getSn() != null) { // Límite vecino
            n = Time.copy(lim.getSn());
            log.d("found Sn min= " + n + "= (limitSn)" + lim.getSn());
        }


        // limit X
        log.d("Findind Max...");
        if (v.getDx() != null && v.getFx() != null) { // Determinantes
            x = Time.sub(v.getFx(), v.getDx());
            log.d("found Sn max= " + x + "= (Fx)" + v.getFx() + " - (Dx)" + v.getDx());
        } else {
            List<Time> maxs = new ArrayList<>();

            maxs.add(lim.getFn()); // Límite opuesto
            log.d("limitFn= " + lim.getFn());
            if (isFlexible) { // Gemela lado contrario limitante (FLEXIBLE solo)
                maxs.add(v.getSx());
                log.d("Sx= " + v.getSx());
                if (v.getFn() != null && v.getDn() != null) { // Calculada
                    Time t = Time.sub(v.getFn(), v.getDn());
                    maxs.add(t);
                    log.d("Fn - Dn = " + t);
                }
            }
            maxs.add(v.getFn()); // Variable opuesta
            log.d("Fn= " + v.getFn());
            maxs.add(v.getFx()); // Variable opuesta lado contrario
            log.d("Fx= " + v.getFx());
            if (v.getDn() != null) { // Otra variable afectante: Dn
                if (v.getFn() != null) {
                    Time t = Time.sub(v.getFn(), v.getDn());
                    maxs.add(t);
                    log.d("Fn-Dn= " + t);
                }
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), v.getDn());
                    maxs.add(t);
                    log.d("Fx-Dn= " + t);
                }
                if (lim.getFn() != null) {
                    Time t = Time.sub(lim.getFn(), v.getDn());
                    maxs.add(t);
                    log.d("Fn-Dn= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), v.getDn());
                    maxs.add(t);
                    log.d("Fx-Dn= " + t);
                }
            }

            x = Time.findMinorValue(maxs);
            if (x != null)
                log.d("found Sx max= " + x + " (minor of above)");
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);
        log.i("END calcMinMaxSn(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxSx(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxSx(). activity= " + ac + ", isFlexible= " + isFlexible);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n, x;

        addScheduleMargins(lim);

        // limit S
        log.d("Findind Min...");
        if (v.getDn() != null && v.getFn() != null) { // Determinantes
            n = Time.sub(v.getFn(), v.getDn());
            log.d("found Sx min= " + n + "= (Fn)" + v.getFn() + " - (Dn)" + v.getDn());

        } else {
            List<Time> mins = new ArrayList<>();
            mins.add(lim.getSx()); // Límite natural
            log.d("LimitSx= " + lim.getSx());
            if (isFlexible) { //Gemela lado contrario limitante (FLEXIBLE solo)
                mins.add(v.getSn());
                log.d("Sn= " + v.getSn());
                if (v.getFx() != null && v.getDx() != null) { // Calculada
                    Time t = Time.sub(v.getFx(), v.getDx());
                    mins.add(t);
                    log.d("Fx-Dx= " + t);
                }
            }
            mins.add(lim.getSn()); // Límite natural lado contrario
            log.d("LimitSn= " + lim.getSn());

            n = Time.findMajorValue(mins);
            if (n != null)
                log.d("found Sx min= " + n + " (major of above)");
        }

        // limit X
        log.d("Findind Max...");
        if (v.getDn() != null && v.getFn() != null) { // Determinantes
            x = Time.sub(v.getFn(), v.getDn());
            log.d("found Sx max= " + x + "= (Fn)" + v.getFn() + " - (Dn)" + v.getDn());

        } else {
            List<Time> maxs = new ArrayList<>();
            maxs.add(lim.getFx()); // Límite opuesto
            log.d("LimitFx= " + lim.getFx());
            maxs.add(v.getFx()); // Var opuesta
            log.d("Fx= " + v.getFx());
            maxs.add(lim.getFn()); // Límite opuesto lado contrario
            log.d("LimitFn= " + lim.getFn());
            maxs.add(v.getFn()); // Var opuesta lado contrario
            log.d("Fn= " + v.getFn());
            if (v.getDn() != null) { // Variable lado contrario afectante; Dn
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), v.getDn());
                    maxs.add(t); // con final
                    log.d("Fx-Dn= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), v.getDn());
                    maxs.add(t); // con límite op
                    log.d("limitFx-Dn= " + t);
                }
                if (lim.getFn() != null) {
                    Time t = Time.sub(lim.getFn(), v.getDn());
                    maxs.add(t); // límite vecino
                    log.d("limitFn-Dn= " + t);
                }
            }

            x = Time.findMinorValue(maxs);
            if (x != null) log.d("found Sx max= " + x + " (minor of above)");
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);

        log.i("END calcMinMaxS(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxDn(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxDn(). activity= " + ac + ", isFlexible= " + isFlexible);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n = null, x = null;

        addScheduleMargins(lim);

        // limit S
        log.d("Findind Min...");
        if (v.getSx() != null && v.getFn() != null) { // Determinantes
            n = Time.sub(v.getFn(), v.getSx());
            log.d("found Dn min= " + n + "= (Fn)" + v.getFn() + " - (Sx)" + v.getSx());
        }

        // limit X
        log.d("Findind Max...");
        if (v.getSx() != null && v.getFn() != null) { // Determinantes
            x = Time.sub(v.getFn(), v.getSx());
            log.d("found Dn max= " + x + "= (Fn)" + v.getFn() + " - (Sx)" + v.getSx());

        } else {
            List<Time> maxs = new ArrayList<>();
//            maxs.addAll(calcAllDurations(ac));  // Todas las duraciones calculables

            if (v.getSx() != null) {
                if (v.getFn() != null) {
                    Time t = Time.sub(v.getFn(), v.getSx());
                    maxs.add(t);
                    log.d("Fn-Sx= " + t);
                }
                if (lim.getFn() != null) {
                    Time t = Time.sub(lim.getFn(), v.getSx());
                    maxs.add(t);
                    log.d("limitFn-Sx= " + t);
                }
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), v.getSx());
                    maxs.add(t);
                    log.d("Fx-Sx= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), v.getSx());
                    maxs.add(t);
                    log.d("limitFx-Sx= " + t);
                }
            }
            if (lim.getSx() != null) {
                if (v.getFn() != null) {
                    Time t = Time.sub(v.getFn(), lim.getSx());
                    maxs.add(t);
                    log.d("Fn-limitSx= " + t);
                }
                if (lim.getFn() != null) {
                    Time t = Time.sub(lim.getFn(), lim.getSx());
                    maxs.add(t);
                    log.d("limitFn-limitSx= " + t);
                }
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), lim.getSx());
                    maxs.add(t);
                    log.d("Fx-limitSx= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), lim.getSx());
                    maxs.add(t);
                    log.d("limitFx-limitSx= " + t);
                }
            }
            if (v.getSn() != null) {
                if (v.getFn() != null) {
                    Time t = Time.sub(v.getFn(), v.getSn());
                    maxs.add(t);
                    log.d("Fn-Sn= " + t);
                }
                if (lim.getFn() != null) {
                    Time t = Time.sub(lim.getFn(), v.getSn());
                    maxs.add(t);
                    log.d("limitFn-Sn= " + t);
                }
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), v.getSn());
                    maxs.add(t);
                    log.d("Fx-Sn= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), v.getSn());
                    maxs.add(t);
                    log.d("limitFx-Sn= " + t);
                }
            }
            if (lim.getSn() != null) {
                if (v.getFn() != null) {
                    Time t = Time.sub(v.getFn(), lim.getSn());
                    maxs.add(t);
                    log.d("Fn-limitSn= " + t);
                }
                if (lim.getFn() != null) {
                    Time t = Time.sub(lim.getFn(), lim.getSn());
                    maxs.add(t);
                    log.d("limitFn-limitSn= " + t);
                }
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), lim.getSn());
                    maxs.add(t);
                    log.d("Fx-limitSn= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), lim.getSn());
                    maxs.add(t);
                    log.d("limitFx-limitSn= " + t);
                }
            }

            if (isFlexible) {
                maxs.add(v.getDx()); // Gemela lado contrario limitante (FLEXIBLE solo)
                log.d("Dx= " + v.getDx());
            }

            x = Time.findMinorValue(maxs);
            if (x != null) log.d("found Dn max= " + x + " (minor of above)");
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);
        log.i("END calcMinMaxDn(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxDx(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxDx(). activity= " + ac + ", isFlexible= " + isFlexible);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n = null, x = null;

        addScheduleMargins(lim);

        // limit S
        log.d("Findind Min...");
        if (v.getSn() != null && v.getFx() != null) { // Determinantes
            n = Time.sub(v.getFx(), v.getSn());
            log.d("found Dx min= " + n + "= (Fx)" + v.getFx() + " - (Sn)" + v.getSn());
        } else if (isFlexible && v.getDn() != null) {
            // Gemela lado contrario limitante (FLEXIBLE solo)
            n = Time.copy(v.getDn());
            log.d("found Dx min= " + n + "= (Dn)" + v.getDn());
        } else if (isFlexible && v.getSx() != null && v.getFn() != null) {
            // Gemela lado contrario limitante (FLEXIBLE solo) calculada
            n = Time.sub(v.getFn(), v.getSx());
            log.d("found Dx min= " + n + "= (Fn)" + v.getFn() + " - (Sx)" + v.getSx());
        }

        // limit X
        log.d("Findind Max...");
        if (v.getSn() != null && v.getFx() != null) { // Determinantes
            x = Time.sub(v.getFx(), v.getSn());
            log.d("found Dx max= " + x + "= (Fx)" + v.getFx() + " - (Sn)" + v.getSn());
        } else {
            List<Time> maxs = new ArrayList<>();
//            maxs.addAll(calcAllDurations(ac)); // Todas las duraciones calculables

            if (v.getSn() != null) {
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), v.getSn());
                    maxs.add(t);
                    log.d("Fx-Sn= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), v.getSn());
                    maxs.add(t);
                    log.d("limitFx-Sn= " + t);
                }
            }
            if (lim.getSn() != null) {
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), lim.getSn());
                    maxs.add(t);
                    log.d("Fx-limitSn= " + t);
                }
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), lim.getSn());
                    maxs.add(t);
                    log.d("limitFx-limitSn= " + t);
                }
            }

            x = Time.findMinorValue(maxs);
            log.d("found Dx max= " + x + " (minor of above)");
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);
        log.i("END calcMinMaxDx(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxFn(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxFn(). activity= " + ac + ", isFlexible= " + isFlexible);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n, x;

        addScheduleMargins(lim);

        // limit S
        log.d("Findind Min...");
        if (v.getSx() != null && v.getDn() != null) { // Determinantes
            n = Time.sum(v.getSx(), v.getDn());
            log.d("found Fn min= " + n + "= (Sx)" + v.getSx() + " + (Dn)" + v.getDn());
        } else {
            List<Time> mins = new ArrayList<>();
            mins.add(v.getSn());// Variable opuesta
            log.d("Sn= " + v.getSn());
            mins.add(lim.getSn());// Límite opuesto
            log.d("limitSn= " + lim.getSn());
            mins.add(v.getSx());// Variable opuesta lado contrario
            log.d("Sx= " + v.getSx());
            mins.add(lim.getSx());// Límite opuesto lado contrario
            log.d("limitSx= " + lim.getSx());
            if (v.getDn() != null) { // Otra variable afectante: Dn
                if (v.getSn() != null) {
                    Time t = Time.sum(v.getSn(), v.getDn());
                    mins.add(t);
                    log.d("Sn+Dn= " + t);
                }
                if (lim.getSn() != null) {
                    Time t = Time.sum(lim.getSn(), v.getDn());
                    mins.add(t);
                    log.d("limitSn+Dn= " + t);
                }
                if (v.getSx() != null) {
                    Time t = Time.sum(v.getSx(), v.getDn());
                    mins.add(t);
                    log.d("Sx+Dn= " + t);
                }
                if (lim.getSx() != null) {
                    Time t = Time.sum(lim.getSx(), v.getDn());
                    mins.add(t);
                    log.d("limitSx+Dn= " + t);
                }
            }

            n = Time.findMajorValue(mins);
            if (n != null) log.d("found Fn min= " + n + " (major of above)");

        }

        // limit X
        log.d("Findind Max...");
        if (v.getSx() != null && v.getDn() != null) { // Determinantes
            x = Time.sum(v.getSx(), v.getDn());
            log.d("found Fn max= " + x + "= (Sx)" + v.getFx() + " - (Dn)" + v.getDx());
        } else {
            List<Time> maxs = new ArrayList<>();

            maxs.add(lim.getFn()); // Límite vecino
            log.d("limitFn= " + lim.getFn());
            if (isFlexible) { // Gemela lado contrario limitante (FLEXIBLE solo)
                maxs.add(v.getFx());
                log.d("Fx= " + v.getFx());
                if (v.getSn() != null && v.getDx() != null) { // Calculada
                    Time t = Time.sum(lim.getSn(), v.getDx());
                    maxs.add(t);
                    log.d("Sn+Dx= " + t);
                }
            }
            maxs.add(lim.getFx()); // Límite vecino contrario
            log.d("limitFx= " + lim.getFx());
            if (v.getDn() != null) { // Otra variable afectante: Dn
                // Var opuesta
                if (v.getSn() != null) {
                    Time t = Time.sum(v.getSn(), v.getDn());
                    maxs.add(t);
                    log.d("Sn-Dn= " + t);
                }
                // Límite opuesto
                if (lim.getSn() != null) {
                    Time t = Time.sum(lim.getSn(), v.getDn());
                    maxs.add(t);
                    log.d("limitSn-Dn= " + t);
                }
            }

            x = Time.findMinorValue(maxs);
            if (x != null) log.d("found Fn max= " + x + " (minor of above)");

        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);
        log.i("END calcMinMaxFn(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxFx(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxFx(). activity= " + ac + ", isFlexible= " + isFlexible);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n, x = null;

        addScheduleMargins(lim);

        // LIMIT S
        log.d("Findind Min...");
        if (v.getSn() != null && v.getDx() != null) { // Determinantes
            n = Time.sum(v.getSn(), v.getDx());
            log.d("found Fx min= " + n + "= (Sn)" + v.getSn() + " + (Dx)" + v.getDx());
        } else {
            List<Time> mins = new ArrayList<>();

            if (isFlexible) { // Gemela lado contrario limitante (FLEXIBLE solo)
                mins.add(v.getFn());
                log.d("Fn= " + v.getFn());
                if (v.getSx() != null && v.getDn() != null) { // Calculada
                    Time t = Time.sum(lim.getSx(), v.getDn());
                    mins.add(t);
                    log.d("limitSx-Dn= " + t);
                }
            }
            mins.add(v.getSx()); // Variable opuesta
            log.d("Sx= " + v.getSx());
            mins.add(lim.getSx()); // Límite opuesto
            log.d("limitSx= " + v.getSx());
            mins.add(v.getSn()); // Variable opuesta lado contrario
            log.d("Sn= " + v.getSn());
            mins.add(lim.getSn()); // Límite opuesto lado contrario
            log.d("limitSn= " + v.getSn());
            if (v.getDn() != null) { // Otra variable afectante: Dn
                if (v.getSx() != null) {
                    Time t = Time.sum(v.getSx(), v.getDn());
                    mins.add(t);
                    log.d("Sx-Dn= " + t);
                }
                if (lim.getSx() != null) {
                    Time t = Time.sum(lim.getSx(), v.getDn());
                    mins.add(t);
                    log.d("limitSx-Dn= " + t);
                }
                if (v.getSn() != null) {
                    Time t = Time.sum(v.getSn(), v.getDn());
                    mins.add(t);
                    log.d("Sn-Dn= " + t);
                }
                if (lim.getSn() != null) {
                    Time t = Time.sum(lim.getSn(), v.getDn());
                    mins.add(t);
                    log.d("limitSn-Dn= " + t);
                }
            }
            if (v.getDx() != null) { // Otra variable afectante: Dx
                // [ on Sn son los determinantes ]
                if (lim.getSn() != null) {
                    Time t = Time.sum(lim.getSn(), v.getDx());
                    mins.add(t);
                    log.d("limitSn-Dx= " + t);
                }
            }

            n = Time.findMajorValue(mins);
            if (n != null) log.d("found Fx min= " + n + " (major of above)");
        }

        // LIMIT X
        log.d("Findind Max...");
        if (v.getSx() != null && v.getDn() != null) { // Determinante
            x = Time.sum(v.getSx(), v.getDn());
            log.d("found Fx max= " + x + "= (Sx)" + v.getFx() + " + (Dn)" + v.getDx());
        } else if (lim.getFx() != null) {
            log.d("found Fx max= " + x + "= (limitFx)" + lim.getFx());
            x = Time.copy(lim.getFx());
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);
        log.i("END calcMinMaxFn(). minMax= " + minMax);
        return minMax;
    }

    public List<Time> calcAllDurations(Activity ac) {
        List<Time> maxs = new ArrayList<>();
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();

        if (v.getSx() != null) {
            if (v.getFn() != null) {
                Time t = Time.sub(v.getFn(), v.getSx());
                maxs.add(t);
                log.d("Fn-Sx= " + t);
            }
            if (lim.getFn() != null) {
                Time t = Time.sub(lim.getFn(), v.getSx());
                maxs.add(t);
                log.d("limitFn-Sx= " + t);
            }
            if (v.getFx() != null) {
                Time t = Time.sub(v.getFx(), v.getSx());
                maxs.add(t);
                log.d("Fx-Sx= " + t);
            }
            if (lim.getFx() != null) {
                Time t = Time.sub(lim.getFx(), v.getSx());
                maxs.add(t);
                log.d("limitFx-Sx= " + t);
            }
        }
        if (lim.getSx() != null) {
            if (v.getFn() != null) {
                Time t = Time.sub(v.getFn(), lim.getSx());
                maxs.add(t);
                log.d("Fn-limitSx= " + t);
            }
            if (lim.getFn() != null) {
                Time t = Time.sub(lim.getFn(), lim.getSx());
                maxs.add(t);
                log.d("limitFn-limitSx= " + t);
            }
            if (v.getFx() != null) {
                Time t = Time.sub(v.getFx(), lim.getSx());
                maxs.add(t);
                log.d("Fx-limitSx= " + t);
            }
            if (lim.getFx() != null) {
                Time t = Time.sub(lim.getFx(), lim.getSx());
                maxs.add(t);
                log.d("limitFx-limitSx= " + t);
            }
        }
        if (v.getSn() != null) {
            if (v.getFn() != null) {
                Time t = Time.sub(v.getFn(), v.getSn());
                maxs.add(t);
                log.d("Fn-Sn= " + t);
            }
            if (lim.getFn() != null) {
                Time t = Time.sub(lim.getFn(), v.getSn());
                maxs.add(t);
                log.d("limitFn-Sn= " + t);
            }
            if (v.getFx() != null) {
                Time t = Time.sub(v.getFx(), v.getSn());
                maxs.add(t);
                log.d("Fx-Sn= " + t);
            }
            if (lim.getFx() != null) {
                Time t = Time.sub(lim.getFx(), v.getSn());
                maxs.add(t);
                log.d("limitFx-Sn= " + t);
            }
        }
        if (lim.getSn() != null) {
            if (v.getFn() != null) {
                Time t = Time.sub(v.getFn(), lim.getSn());
                maxs.add(t);
                log.d("Fn-limitSn= " + t);
            }
            if (lim.getFn() != null) {
                Time t = Time.sub(lim.getFn(), lim.getSn());
                maxs.add(t);
                log.d("limitFn-limitSn= " + t);
            }
            if (v.getFx() != null) {
                Time t = Time.sub(v.getFx(), lim.getSn());
                maxs.add(t);
                log.d("Fx-limitSn= " + t);
            }
            if (lim.getFx() != null) {
                Time t = Time.sub(lim.getFx(), lim.getSn());
                maxs.add(t);
                log.d("limitFx-limitSn= " + t);
            }
        }
        return maxs;
    }

    private void addScheduleMargins(Limits lim) {
        if (lim.getSn() == null) lim.setSn(Time.copy(scheStart));
        if (lim.getSx() == null) lim.setSx(Time.copy(scheStart));
        if (lim.getFn() == null) lim.setFn(Time.copy(scheFin));
        if (lim.getFx() == null) lim.setFn(Time.copy(scheFin));
    }

    public NX calcNoFlexibleMinMax(String var, Activity ac) {
        log.i("BEGIN calcNoFlexibleMinMaxS(). var= " + var + ", activity= " + ac);
        NX minMax = new NX();
        ActivityVars c = ac.getConfigVars();
        Limits l = ac.getLimits();

        NX min = null, max = null;
        switch (var) {
            case K.S:
                min = calcMinMaxSn(ac, false);
                max = calcMinMaxSx(ac, false);
                break;
            case K.D:
                min = calcMinMaxDn(ac, false);
                max = calcMinMaxDx(ac, false);
                break;
            case K.F:
                min = calcMinMaxFn(ac, false);
                max = calcMinMaxFx(ac, false);
                break;
            default:
                throw new IllegalArgumentException("Invalid var argument.");
        }
        log.d("min= " + min + ", max= " + max);

        minMax.setN(Time.findMajorValue(min.getN(), max.getN()));
        minMax.setX(Time.findMinorValue(min.getX(), max.getX()));

        log.i("END calcNoFlexibleMinMaxS(). var= " + var + ", minMax= " + minMax);
        return minMax;
    }

    /**
     * Comprueba si una variable de una actividad puede ser establecida como no flexible, es
     * decir, que sus variables máximas y mínimas sean iguales y se traten como una sola.
     *
     * <p> Para ello, comprueba que los valores de validación de una variable sean compatibles entre
     * sus lados máximo y mínimo. En concreto, se comprueba si alguno de los extremos de los
     * valores de validación de las variable del lado mínimo se encuentra dentro del rango de
     * los valores de valiación del lado máximo.
     *
     * @param var      la variable de la actividad que se desea evaluar ( {@link K#S},
     *                 {@link K#D} o {@link K#F} ).
     * @param activity la actividad que contiene la variable que se desea evaluar.
     * @return true si puede ser flexible, false si no.
     */
    public boolean validateDisableFlexible(String var, Activity activity) {
        log.i("BEGIN validateDisableFlexible(). var= " + var + ", activity= " + activity);
        boolean can = false;
        NX sideN;
        NX sideX;


        switch (var) {
            case K.S:
                sideN = calcMinMaxSn(activity, false);
                sideX = calcMinMaxSx(activity, false);
                break;
            case K.D:
                sideN = calcMinMaxDn(activity, false);
                sideX = calcMinMaxDx(activity, false);
                break;
            case K.F:
                sideN = calcMinMaxFn(activity, false);
                sideX = calcMinMaxFx(activity, false);
                break;
            default:
                throw new IllegalArgumentException();
        }
        log.d("sideN= " + sideN + ", sideX= " + sideX);


        if (sideN.getN() == null && sideN.getX() == null) {
            log.d(var + ": sideNMin = null & sideNMax = null");
            can = true;
        } else if (var.equals(K.D)) {
            // Duraciones
            if (sideN.getX() == null || sideX.getN() == null) {
                log.d(var + ": sideNMin = null || sideXMin = null");
                can = true;
            } else if (sideN.getX().greaterOrEqualTo(sideX.getN())) {
                log.d(var + ": (sideNMax)" + sideN.getX() + " >= (sideXMin)" + sideX.getN());
                can = true;
            }
        } else {
            // Inicios y finales
            if (sideN.getN() != null && sideN.getN().isInRange(sideX.getN(), sideX.getX())) {
                log.d(var + ": (sideXMin)" + sideX.getN() + " <= (sideNMin)" + sideN.getN() + " <= " +
                        "(sideXMax)" + sideX.getX());
                can = true;
            } else if (sideN.getX() != null && sideN.getX().isInRange(sideX.getN(), sideX.getX())) {
                log.d(var + ": (sideXMin)" + sideX.getN() + " <= (sideNMax)" + sideN.getX() + " <= " +
                        "(sideXMax)" + sideX.getX());
                can = true;
            }
        }

        log.i("END validateDisableFlexible(). var= " + var + ", validateDisableFlexible= " + can);
        return can;
    }


}

