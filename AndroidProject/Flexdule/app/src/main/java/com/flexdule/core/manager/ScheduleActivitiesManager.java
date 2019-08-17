package com.flexdule.core.manager;

import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.ActivityVars;
import com.flexdule.core.dtos.Limits;
import com.flexdule.core.dtos.NX;
import com.flexdule.core.util.CK;
import com.flexdule.core.util.CU;
import com.flexdule.core.util.CoreLog;
import com.flexdule.core.util.Time;

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

            // Se calculan las variables finales en base a las de configuración
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

        log.d("calcContext() activities = " + acs);
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

                if (fin.getSn() != null
                        && fin.getDn() != null
                        && !Time.sum(fin.getSn(), fin.getDn()).equals(conf.getFn())) {
                    log.w("Fn != Sn+Dn !");
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
                if (fin.getSx() != null && fin.getDx() != null
                        && !Time.sum(fin.getSx(), fin.getDx()).equals(conf.getFx())) {
                    log.w("Fx != Sx+Dx !");
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
            Time foundSn = calcS("Sn", v.getSn(), v.getDn(), v.getFn());
            if (foundSn != null) {
                v.setSn(foundSn);
                found = true;
            }
            Time foundDn = calcD("Dn", v.getSn(), v.getDn(), v.getFn());
            if (foundDn != null) {
                v.setDn(foundDn);
                found = true;
            }
            Time foundFn = calcF("Fn", v.getSn(), v.getDn(), v.getFn());
            if (foundFn != null) {
                v.setFn(foundFn);
                found = true;
            }
            Time foundSx = calcS("Sx", v.getSx(), v.getDx(), v.getFx());
            if (foundSx != null) {
                v.setSx(foundSx);
                found = true;
            }
            Time foundDx = calcD("Dx", v.getSx(), v.getDx(), v.getFx());
            if (foundDx != null) {
                v.setDx(foundDx);
                found = true;
            }
            Time foundFx = calcF("Fx", v.getSx(), v.getDx(), v.getFx());
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

//                // Límites iniciales
//                if (i != 0) {
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
//                } else {
//                    // Para la primera actividad, se establece el límite inicial del horario
//                    l.setSn(Time.copy(scheStart));
//                    l.setSx(Time.copy(scheStart));
//                }

//                // Límites finales
//                if (i != acs.size() - 1) {
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
//                } else {
//                    // Para la última actividad, se establece el límite máximo del horario
//                    l.setFx(Time.copy(scheFin));
//                }
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

    public Time calcS(String tag, Time s, Time d, Time f) {
        Time found = null;

        if (s == null) {
            if (f != null && d != null) {
                found = Time.sub(f, d);
//                if( s.lessThan(scheStart) ) s = Time.copy(scheStart);
                log.i("found " + tag + "= " + found + "= (f)" + f + " - (d)" + d);
            }
        }
        return found;
    }

    public Time calcD(String tag, Time s, Time d, Time f) {
        Time found = null;
        if (d == null) {
            if (f != null && s != null) {
                found = Time.sub(f, s);
                log.i("found " + tag + "= " + found + "= (f)" + f + " - (s)" + s);
            }
        }
        return found;
    }

    public Time calcF(String tag, Time s, Time d, Time f) {
        Time found = null;
        if (f == null) {
            if (s != null && d != null) {
                found = Time.sum(s, d);
                log.i("found " + tag + "= " + found + "= (s)" + s + " + (d)" + d);
            }
        }
        return found;
    }

    public Time calcSnWithLimit(Time sn, Time limitSn) {
        Time found = null;
        if (sn == null) {
            if (limitSn != null /*&& !limitSn.equals(scheStart)*/) {
                found = Time.copy(limitSn);
                log.i("found Sn= " + found + "= (limitSn)" + limitSn);
            }
        }
        return found;
    }

    public Time calcFxWithLimit(Time fx, Time limitFx) {
        Time found = null;
        if (fx == null) {
            if (limitFx != null /*&& !limitFx.equals(scheFin)*/) {
                found = Time.copy(limitFx);
                log.i("found Fx= " + found + "= (limitFx)" + limitFx);
            }
        }
        return found;
    }

    // Los límites no se afectan entre lados ( N <-> X ). Esas limitaciones del ámbito del minMax.
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
                    found = Time.copy(v.getFn());
                }
                if (found == null && v.getSx() != null) {
                    found = Time.copy(v.getSn());
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

    public NX calcMinMaxS(String tag, Activity ac, String side, boolean isNoFlexible) {
        log.i("BEGIN calcMinMaxS(). " + tag + ", activity= " + ac + ", side=" + side +
                "isNoFlexible= " + isNoFlexible);
        Time n = null, x = null;
        ActivityVars v = ac.getConfigVars();
        Limits l = ac.getLimits();
        Time s, d, f, limitS, limitF;
        switch (side) {
            case CK.N:
                s = v.getSn();
                d = v.getDn();
                f = v.getFn();
                limitS = l.getSn();
                limitF = l.getFn();
                break;
            case CK.X:
                s = v.getSx();
                d = v.getDx();
                f = v.getFx();
                limitS = l.getSx();
                limitF = l.getFx();
                break;
            default:
                throw new IllegalArgumentException();
        }

        if (limitS == null) limitS = Time.copy(scheStart);
        if (limitF == null) limitF = Time.copy(scheFin);

        // limit S
        if (d != null && f != null) {
            n = Time.sub(f, d);
            log.d("found " + tag + " min= " + n + "= (f)" + f + " - (d)" + d);
        }
        if (n == null && CK.X.equals(side) && !isNoFlexible) {
            if (v.getSn() != null) {
                n = Time.copy(v.getSn());
                log.d("found " + tag + " min= " + n + "= (Sn)" + v.getSn());
            }
            if (v.getFn() != null && v.getDn() != null) {
                n = Time.sub(v.getFn(), v.getDn());
                log.d("found " + tag + " min= " + n + "= (Fn)" + v.getFn() + " - (Dn)" + v.getDn());
            }
        }
        if (n == null && CK.X.equals(side) && !isNoFlexible) {
            n = Time.findMajorValue(limitS, l.getSn());
            log.d("found " + tag + " min= " + n + "= major(limitSn[" + l.getSn() + "], limitSx[" + l.getSx() + "])");
        }
        if (n == null && limitS != null) {
            n = Time.copy(limitS);
            log.d("found " + tag + " min= " + n + "= (limitS)" + limitS);
        }

        // limit X
        if (d != null && f != null) {
            x = Time.sub(f, d);
            log.d("found " + tag + " max= " + x + "= (f)" + f + " - (d)" + d);
        } else if (d != null && limitF != null) {
            x = Time.sub(limitF, d);
            log.d("found " + tag + " max= " + x + "= (limitF)" + limitF + " - (d)" + d);
        } else if (f != null) {
            x = Time.copy(f);
            log.d("found " + tag + " max= " + x + "= (f)" + f);
        } else if (limitF != null) {
            x = Time.copy(limitF);
            log.d("found " + tag + " max= " + x + "= (limitF)" + limitF);
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);

        log.i("END calcMinMaxS(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxS(String tag, Activity ac, String side) {
        return calcMinMaxS(tag, ac, side, false);
    }

    public NX calcMinMaxD(String tag, Activity ac, String side, boolean isNoFlexible) {
        log.i("BEGIN calcMinMaxD(). " + tag + ", activity= " + ac + ", side=" + side + ", " +
                "isNoFlexible= " + isNoFlexible);
        Time n = null, x = null;
        ActivityVars v = ac.getConfigVars();
        Limits l = ac.getLimits();
        Time s, d, f, limitS, limitF;
        switch (side) {
            case CK.N:
                s = v.getSn();
                d = v.getDn();
                f = v.getFn();
                limitS = l.getSn();
                limitF = Time.findMinorValue(l.getFn(), l.getFx());
                break;
            case CK.X:
                s = v.getSx();
                d = v.getDx();
                f = v.getFx();
                limitS = Time.findMajorValue(l.getSn(), l.getSx());
                limitF = ac.getLimits().getFx();
                break;
            default:
                throw new IllegalArgumentException();
        }

        if (limitS == null) limitS = Time.copy(scheStart);
        if (limitF == null) limitF = Time.copy(scheFin);

        // limit S
        if (s != null && f != null) {
            n = Time.sub(f, s);
            log.d("found " + tag + " min= " + n + "= (f)" + f + " - (s)" + s);
        }

        // limit X
        if (s != null && f != null) {
            x = Time.sub(f, s);
            log.d("found " + tag + " max= " + x + "= (f)" + f + " - (s)" + s);
        } else if (s != null && limitF != null) {
            x = Time.sub(limitF, s);
            log.d("found " + tag + " max= " + x + "= (limitF)" + limitF + " - (s)" + s);
        } else if (f != null && limitS != null) {
            x = Time.sub(f, limitS);
            log.d("found " + tag + " max= " + x + "= (f)" + f + " - (limitS)" + limitS);
        } else if (limitS != null && limitF != null) {
            x = Time.sub(limitF, limitS);
            log.d("found " + tag + " max= " + x + "= (limitF)" + limitF + " - (limitS)" + limitS);
        }
        if (side.equals(CK.N) && v.getDx() != null && !isNoFlexible) {
            if (v.getDx().compareTo(x) < 0) {
                log.d("found " + tag + " max= " + v.getDx() + "= (Dx)" + v.getDx() + " < (max)" + x);
                x = Time.copy(v.getDx());
            }
        }
        if (side.equals(CK.X) && !isNoFlexible) {
            Time DnMax = calcMinMaxD("[Dn for Dx]", ac, CK.N,false).getX();
            if (DnMax.compareTo(x) > 0) {
                log.d("found " + tag + " max= " + DnMax + "= (DnMax)" + DnMax + " > (max)" + x);
                x = Time.copy(DnMax);
            }
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);

        log.i("END calcMinMaxD(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxD(String tag, Activity ac, String side) {
        return calcMinMaxD(tag, ac, side, false);
    }

    public NX calcMinMaxF(String tag, Activity ac, String side, boolean isNoFlexible) {
        log.i("BEGIN calcMinMaxF(). " + tag + ", activity= " + ac + ", side=" + side +
                "isNoFlexible= " + isNoFlexible);
        Time n = null, x = null;
        ActivityVars v = ac.getConfigVars();
        Limits l = ac.getLimits();
        Time s, d, f, limitS, limitF;
        switch (side) {
            case CK.N:
                s = v.getSn();
                d = v.getDn();
                f = v.getFn();
                limitS = l.getSn();
                limitF = l.getFn();
                break;
            case CK.X:
                s = v.getSx();
                d = v.getDx();
                f = v.getFx();
                limitS = ac.getLimits().getSx();
                limitF = ac.getLimits().getFx();
                break;
            default:
                throw new IllegalArgumentException();
        }

        if (limitS == null) limitS = Time.copy(scheStart);
        if (limitF == null) limitF = Time.copy(scheFin);

        // limit S
        if (s != null && d != null) {
            n = Time.sum(s, d);
            log.d("found " + tag + " min= " + n + "= (s)" + s + " + (d)" + d);
        } else if (limitS != null && d != null) {
            n = Time.sum(s, limitS);
            log.d("found " + tag + " min= " + n + "= (limitS)" + limitS + " + (d)" + d);
        } else if (s != null) {
            n = Time.copy(s);
            log.d("found " + tag + " min= " + n + "= (s)" + s);
        } else if (limitS != null) {
            n = Time.copy(limitS);
            log.d("found " + tag + " min= " + n + "= (limitS)" + limitS);
        }

        // limit X
        if (s != null && d != null) {
            x = Time.sum(s, d);
            log.d("found " + tag + " max= " + x + "= (s)" + s + " + (d)" + d);
        }
        if (x == null && CK.N.equals(side) && !isNoFlexible) {
            if (v.getFx() != null) {
                x = Time.copy(v.getFx());
                log.d("found " + tag + " max= " + x + "= (Fx)" + v.getFx());
            } else if (v.getSx() != null && v.getDx() != null) {
                x = Time.sum(v.getSx(), v.getDx());
                log.d("found " + tag + " max= " + x + "= (Sx)" + v.getSx() + " + (Dx)" + v.getDx());
            } else {
                x = Time.findMinorValue(limitF, l.getFx());
                log.d("found " + tag + " max= " + x + "= minor(limitFn[" + limitF + "], limitFx[" + l.getFx() + "])");
            }
        }else if (x == null && limitF != null) {
            x = Time.copy(limitF);
            log.d("found " + tag + " max= " + x + "= (limitF)" + limitF);
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);

        log.i("END calcMinMaxF(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxF(String tag, Activity ac, String side) {
        return calcMinMaxF(tag, ac, side, false);
    }

    public NX calcNoFlexibleMinMax(String var, Activity ac) {
        log.i("BEGIN calcNoFlexibleMinMaxS(). var= " + var + ", activity= " + ac);
        NX minMax = new NX();
        ActivityVars c = ac.getConfigVars();
        Limits l = ac.getLimits();

        NX min = null, max = null;
        switch (var) {
            case CK.S:
                min = calcMinMaxS(CU.getNTag(var), ac, CK.N, true);
                max = calcMinMaxS(CU.getXTag(var), ac, CK.X, true);
                break;
            case CK.D:
                min = calcMinMaxD(CU.getNTag(var), ac, CK.N, true);
                max = calcMinMaxD(CU.getXTag(var), ac, CK.X, true);
                break;
            case CK.F:
                min = calcMinMaxF(CU.getNTag(var), ac, CK.N, true);
                max = calcMinMaxF(CU.getXTag(var), ac, CK.X, true);
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
     * @param var      la variable de la actividad que se desea evaluar ( {@link CK#S},
     *                 {@link CK#D} o {@link CK#F} ).
     * @param activity la actividad que contiene la variable que se desea evaluar.
     * @return true si puede ser flexible, false si no.
     */
    public boolean validateDisableFlexible(String var, Activity activity) {
        log.i("BEGIN validateDisableFlexible(). var= " + var + ", activity= " + activity);
        boolean can = false;
        NX sideN;
        NX sideX;


        switch (var) {
            case CK.S:
                sideN = calcMinMaxS(CU.getNTag(var), activity, CK.N, true);
                sideX = calcMinMaxS(CU.getXTag(var), activity, CK.X, true);
                break;
            case CK.D:
                sideN = calcMinMaxD(CU.getNTag(var), activity, CK.N, true);
                sideX = calcMinMaxD(CU.getXTag(var), activity, CK.X, true);
                break;
            case CK.F:
                sideN = calcMinMaxF(CU.getNTag(var), activity, CK.N, true);
                sideX = calcMinMaxF(CU.getXTag(var), activity, CK.X, true);
                break;
            default:
                throw new IllegalArgumentException();
        }
        log.d("sideN= " + sideN + ", sideX= " + sideX);


        if (sideN.getN() == null && sideN.getX() == null) {
            log.d(var + ": sideNMin = null & sideNMax = null");
            can = true;
        } else if (var.equals(CK.D)) {
            // Duraciones
            if (sideN.getN() == null) {
                log.d(var + ": sideNMin = null");
                can = true;
            } else if (sideN.getN().lessOrEqualTo(sideX.getN())) {
                log.d(var + ": sideNMin)" + sideN.getN() + " <= (sideXMin)" + sideX.getN());
                can = true;
            }
        } else {
            // Inicios y finales
            if (sideN.getN() != null && sideN.getN().isInRange(sideX.getN(), sideX.getX())) {
                log.d(var + ": (sideXMin)" + sideX.getN() + " < (sideNMin)" + sideN.getN() + " < " +
                        "(sideXMax)" + sideX.getX());
                can = true;
            } else if (sideN.getX() != null && sideN.getX().isInRange(sideX.getN(), sideX.getX())) {
                log.d(var + ": (sideXMin)" + sideX.getN() + " < (sideNMax)" + sideN.getX() + " < " +
                        "(sideXMax)" + sideX.getX());
                can = true;
            }
        }

        log.i("END validateDisableFlexible(). var= " + var + ", validateDisableFlexible= " + can);
        return can;
    }


}

