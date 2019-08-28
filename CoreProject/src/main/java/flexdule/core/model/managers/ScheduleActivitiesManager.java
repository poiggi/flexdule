package flexdule.core.model.managers;

import java.util.ArrayList;
import java.util.List;

import flexdule.core.dtos.Activity;
import flexdule.core.dtos.ActivityVars;
import flexdule.core.dtos.Limits;
import flexdule.core.dtos.NX;
import flexdule.core.util.CoreLog;
import flexdule.core.util.K;
import flexdule.core.util.Time;

public class ScheduleActivitiesManager {
    private CoreLog log;


    private Time scheStart = new Time("00:00");
    private Time scheFin = new Time("23:59");

    public ScheduleActivitiesManager(CoreLog log) {
        this.log = log;
    }

    public void calcContext(List<Activity> acs) {
        log.i("BEGIN calcContext()");
        for (Activity ac : acs) {
            log.d("begin calcContext: " + ac.toString());
        }

		// Se extraen las variables finales de las de configuración
        for (Activity ac : acs) {
            calcFinalFromConfig(ac);
        }

        boolean found;

        // Se calculan las variables internas que se puedan derivar de las existentes
        int c = 0;
        do {
            found = calcActivitiesInnerTimes(acs);
            c++;
            log.i("Calc inner. c= " + c + ", found= " + found);
        } while (found);
        for (Activity ac : acs) {
            log.d("end calcInnerTimes: " + ac.toString());
        }

        // Se calculan los límites
        calcLimits(acs);
        for (Activity ac : acs) {
            log.d("end calcLimits: " + ac.toString());
        }


        // Por separado, para no afectar a los límites calculados, se calculan las variables
        // internas teniendo en cuenta los límites calculados previamente
        c = 0;
        do {
            boolean limits = calcInnerWithMinMaxs(acs);
//            boolean inner = calcActivitiesInnerTimes(acs);
            found = limits;

            c++;
            log.i("Calc innerWithLimtis. c= " + c + ", found= " + found);
        } while (found);
        for (Activity ac : acs) {
            log.d("end innerWithLimtis: " + ac.toString());
        }

        log.i("END calcContext()");
    }

    public void cleanContext(List<Activity> acs){
        log.i("BEGIN cleanContext()");
        for (Activity ac : acs) {
            ac.setLimits(null);
            ac.setFinalVars(null);
        }
        log.i("END cleanContext()");
    }

    public void calcFinalFromConfig(Activity ac) {
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
//            if (conf.getDx() != null) {
//                boolean validDx = true;
//                if (fin.getDn() != null && conf.getDx().lessThan(fin.getDn())) {
//                    log.w("Dx < Dn !");
//                    validDx = false;
//                }
//                if (validDx)
                    fin.setDx(Time.copy(conf.getDx()));
//            }

            //Fn
            if (conf.getFn() != null) {
                boolean validFn = true;
                if (fin.getSn() != null && conf.getFn().lessThan(fin.getSn())) {
                    log.w("Fn < Sn !");
                    validFn = false;
                }
//                if (fin.getSx() != null && conf.getFn().lessThan(fin.getSx())) {
//                    log.w("Fn < Sx !");
//                    validFn = false;
//                }
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

    public boolean calcActivitiesInnerTimes(List<Activity> acs) {
        boolean found = false;
        log.i("BEGIN calcActivitiesInnerTimes()");
        try {
            for (int i = 0; i < acs.size(); i++) {
                boolean foundI = calcActivityVars(acs.get(i), K.FINAL);
                found = found || foundI;
            }
        } catch (Exception e) {
            log.e("Error in calcActivitiesInnerTimes(): " + e);
            throw e;
        }
        log.i("END calcActivitiesInnerTimes(). found= " + found);
        return found;
    }

    public boolean calcActivityVars(Activity activity, String dimension) {
        log.i("BEGIN calcActivityVars(). activity= " + activity);
        boolean found = false;
        ActivityVars v;

        switch (dimension) {
            case K.FINAL:
                v = activity.getFinalVars();
                break;
            case K.CONF:
                v = activity.getConfigVars();
                break;
            default:
                throw new IllegalArgumentException();
        }
        Time foundSn = calcSn(v);
        if (foundSn != null) {
            v.setSn(foundSn);
            found = true;
        }
        Time foundDn = calcDn(v);
        if (foundDn != null) {
            v.setDn(foundDn);
            found = true;
        }
        Time foundFn = calcFn(v);
        if (foundFn != null) {
            v.setFn(foundFn);
            found = true;
        }
        Time foundSx = calcSx(v);
        if (foundSx != null) {
            v.setSx(foundSx);
            found = true;
        }
        Time foundDx = calcDx(v);
        if (foundDx != null) {
            v.setDx(foundDx);
            found = true;
        }
        Time foundFx = calcFx(v);
        if (foundFx != null) {
            v.setFx(foundFx);
            found = true;
        }

        log.i("END calcActivityVars(). activity= " + activity);
        return found;
    }

    public void calcLimits(List<Activity> acs) {
        log.i("BEGIN calcOuterLimits()");

        for (int i = 0; i < acs.size(); i++) {
            Limits l = acs.get(i).getLimits();


            Time foundLSn = calcLimitSn(acs, i);
            if (foundLSn != null) {
                l.setSn(foundLSn);
            }


            Time foundLSx = calcLimitSx(acs, i);
            if (foundLSx != null) {
                l.setSx(foundLSx);
            }

            Time foundLFn = calcLimitFn(acs, i);
            if (foundLFn != null) {
                l.setFn(foundLFn);
            }

            Time foundLFx = calcLimitFx(acs, i);
            if (foundLFx != null) {
                l.setFx(foundLFx);
            }
        }
        log.i("END calcOuterLimits().");
    }


    public boolean calcInnerWithMinMaxs(List<Activity> acs) {
        log.i("BEGIN calcInnerWithMinMaxs().");
        boolean found = false;
        try {

            for (Activity ac : acs) {
                ActivityVars v = ac.getFinalVars();

                log.i("calcInnerWithMinMaxs() for activity= " + ac);

                if (v.getSn() == null) {
                    Time t = calcMinMaxSn(ac, true).getN();
                    if (t != null) {
                        found = true;
                        v.setSn(t);
                    }
                }

                if (v.getSx() == null) {
                    Time t = calcMinMaxSx(ac, true).getX();
                    if (t != null) {
                        found = true;
                        v.setSx(t);
                    }
                }

                if (v.getDn() == null) {
                    Time t = calcMinMaxDn(ac, true).getN();
                    if (t != null) {
                        found = true;
                        v.setDn(t);
                    }
                }

                if (v.getDx() == null) {
                    Time t = calcMinMaxDx(ac, true).getX();
                    if (t != null) {
                        found = true;
                        v.setDx(t);
                    }
                }

                if (v.getFn() == null) {
                    Time t = calcMinMaxFn(ac, true).getN();
                    if (t != null) {
                        found = true;
                        v.setFn(t);
                    }
                }

                if (v.getFx() == null) {
                    Time t = calcMinMaxFx(ac, true).getX();
                    if (t != null) {
                        found = true;
                        v.setFx(t);
                    }
                }

            }
        } catch (Exception e) {
            log.e("Error in calcInnerWithMinMaxs(): " + e);
            throw e;
        }
        log.i("END calcInnerTimeWithLimits(). found= " + found);
        return found;
    }

    public Time calcSn(ActivityVars v) {
        Time sn = null;
        if (v.getSn() == null && v.getFn() != null && v.getDn() != null) {
            sn = Time.sub(v.getFn(), v.getDn());
            log.i("found Sn= " + sn + "= (Fn)" + v.getFn() + " - (Dn)" + v.getDn());
        }
        return sn;
    }

    public Time calcSx(ActivityVars v) {
        Time sx = null;
        if (v.getSx() == null && v.getFx() != null && v.getDx() != null) {
            sx = Time.sub(v.getFx(), v.getDx());
            log.i("found Sx= " + sx + "= (Fx)" + v.getFx() + " - (Dx)" + v.getDx());
        }
        return sx;
    }

    public Time calcDn(ActivityVars v) {
        Time dn = null;
        if (v.getDn() == null && v.getFn() != null && v.getSn() != null) {
            dn = Time.sub(v.getFn(), v.getSn());
            log.i("found Dn= " + dn + "= (Fn)" + v.getFn() + " - (Sn)" + v.getSn());
        }
        return dn;
    }

    public Time calcDx(ActivityVars v) {
        Time dx = null;
        if (v.getDx() == null && v.getFx() != null && v.getSx() != null) {
            dx = Time.sub(v.getFx(), v.getSx());
            log.i("found Dx= " + dx + "= (Fx)" + v.getFx() + " - (Sx)" + v.getSx());
        }
        return dx;
    }

    public Time calcFn(ActivityVars v) {
        Time fn = null;
        if (v.getFn() == null && v.getSn() != null && v.getDn() != null) {
            fn = Time.sum(v.getSn(), v.getDn());
            log.i("found Fn= " + fn + "= (Sn)" + v.getSn() + " + (Dn)" + v.getDn());
        }
        return fn;
    }

    public Time calcFx(ActivityVars v) {
        Time fx = null;
        if (v.getFx() == null && v.getSx() != null && v.getDx() != null) {
            fx = Time.sum(v.getSx(), v.getDx());
            log.i("found Fx= " + fx + "= (Sx)" + v.getSx() + " + (Dx)" + v.getDx());
        }
        return fx;
    }

    public Time calcLimitSn(List<Activity> acs, int subjectIndex) {
        Activity sub = acs.get(subjectIndex);
        log.i("BEGIN calcLimitSn(). subjectIndex = " + subjectIndex + " ( " + sub.getName() + " )");
        Time found = null;

        if (subjectIndex != 0) {
            int objectIndex = subjectIndex - 1;
            Activity obj = acs.get(objectIndex);
            ActivityVars objV = obj.getFinalVars();

            if (objV.getFn() != null) {
                found = Time.copy(objV.getFn());
            } else if(objV.getSn() != null){
                found = Time.sum(objV.getDn(), objV.getSn());
            } else {
                log.d("Need to find object limit first...");
                Time objLim = calcLimitSn(acs, objectIndex);
                if (objLim != null)
                    found = Time.sum(objV.getDn(), objLim);
            }
        } else
            found = Time.copy(scheStart);

        log.i("END calcLimitSn(). found= " + found);
        return found;
    }

    public Time calcLimitSx(List<Activity> acs, int subjectIndex) {
        Activity sub = acs.get(subjectIndex);
        log.i("BEGIN calcLimitSx(). subjectIndex = " + subjectIndex + " ( " + sub.getName() + " )");
        Time found = null;

        if (subjectIndex != 0) {
            int objectIndex = subjectIndex - 1;
            Activity obj = acs.get(objectIndex);
            ActivityVars objV = obj.getFinalVars();

            if (objV.getFx() != null) {
                found = Time.copy(objV.getFx());
            } else if(objV.getSx() != null){
                found = Time.sum(objV.getDx(), objV.getSx());
            } else {
                log.d("Need to find object limit first...");
                Time objLim = calcLimitSx(acs, objectIndex);
                if (objLim != null)
                    found = Time.sum(objV.getDx(), objLim);
            }
        } else
            found = Time.copy(scheStart);

        log.i("END calcLimitSx(). found= " + found);
        return found;
    }

    public Time calcLimitFn(List<Activity> acs, int subjectIndex) {
        Activity sub = acs.get(subjectIndex);
        log.i("BEGIN calcLimitFn(). subjectIndex = " + subjectIndex + " ( " + sub.getName() + " )");
        Time found = null;

        if (subjectIndex != acs.size() - 1) {
            int objectIndex = subjectIndex + 1;
            Activity obj = acs.get(objectIndex);
            ActivityVars objV = obj.getFinalVars();

            if (objV.getSn() != null) {
                found = Time.copy(objV.getSn());
            } else if(objV.getFn() != null){
                found = Time.sum(objV.getDn(), objV.getFn());
            } else {
                log.d("Need to find object limit first...");
                Time objLim = calcLimitFn(acs, objectIndex);
                if (objLim != null)
                    found = Time.sub(objLim, objV.getDx());
            }
        } else
            found = Time.copy(scheFin);

        log.i("END calcLimitFn(). found= " + found);
        return found;
    }

    public Time calcLimitFx(List<Activity> acs, int subjectIndex) {
        Activity sub = acs.get(subjectIndex);
        log.i("BEGIN calcLimitFx(). subjectIndex = " + subjectIndex + " ( " + sub.getName() + " )");
        Time found = null;

        if (subjectIndex != acs.size() - 1) {
            int objectIndex = subjectIndex + 1;
            Activity obj = acs.get(objectIndex);
            ActivityVars objV = obj.getFinalVars();

            if (objV.getSx() != null) {
                found = Time.copy(objV.getSx());
            } else if(objV.getFx() != null){
                found = Time.sum(objV.getDx(), objV.getFx());
            } else {
                log.d("Need to find object limit first...");
                Time objLim = calcLimitFx(acs, objectIndex);
                if (objLim != null)
                    found = Time.sub(objLim, objV.getDn());
            }
        } else
            found = Time.copy(scheFin);

        log.i("END calcLimitFx(). found= " + found);
        return found;
    }

    public NX calcMinMaxSn(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxSn(). activity= " + ac + ", isFlexible= " + isFlexible);

        // Se calculan posibles variables de conf derivadas
        ac = new Activity(ac);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n = null, x;

        v.setSn(null);
        if (!isFlexible) v.setSx(null);
        calcActivityVars(ac, K.CONF);

        if (v.getSn() != null) {
            n = Time.copy(v.getSn());
            x = Time.copy(v.getSn());
            log.i("Sn determined");
        } else {
            // limit S
            log.d("Finding Min...");
            if (lim.getSn() != null) { // Límite vecino
                n = Time.copy(lim.getSn());
                log.d("found Sn min= " + n + "= (limitSn)" + lim.getSn());
            }

            // limit X
            log.d("Finding Max...");
            List<Time> maxs = new ArrayList<>();

            maxs.add(lim.getFn()); // Límite opuesto
            log.d("limitFn= " + lim.getFn());
            if (isFlexible && v.getSx() != null) { // Gemela lado contrario
                log.d("Sx= " + v.getSx());
            }
            maxs.add(v.getFn()); // Variable opuesta
            log.d("Fn= " + v.getFn());
            maxs.add(v.getFx()); // Variable opuesta lado contrario
            log.d("Fx= " + v.getFx());
            if (v.getDn() != null) { // Otra variable afectante: Dn
//                if (v.getFn() != null) {
//                    Time t = Time.sub(v.getFn(), v.getDn());
//                    maxs.add(t);
//                    log.d("Fn-Dn= " + t);
//                }
                if (v.getFx() != null) {
                    Time t = Time.sub(v.getFx(), v.getDn());
                    maxs.add(t);
                    log.d("Fx-Dn= " + t);
                }
//                if (lim.getFn() != null) {
//                    Time t = Time.sub(lim.getFn(), v.getDn());
//                    maxs.add(t);
//                    log.d("Fn-Dn= " + t);
//                }
//                if (lim.getFx() != null) {
//                    Time t = Time.sub(lim.getFx(), v.getDn());
//                    maxs.add(t);
//                    log.d("Fx-Dn= " + t);
//                }
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

        // Se calculan posibles variables de conf derivadas
        ac = new Activity(ac);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n, x;

        v.setSx(null);
        if (!isFlexible) v.setSn(null);
        calcActivityVars(ac, K.CONF);

        if (v.getSx() != null) {
            n = Time.copy(v.getSx());
            x = Time.copy(v.getSx());
            log.i("Sx determined");
        } else {
            // limit S
            log.d("Finding Min...");

            List<Time> mins = new ArrayList<>();
            mins.add(lim.getSx()); // Límite natural
            log.d("LimitSx= " + lim.getSx());
            mins.add(v.getSn()); //Gemela lado contrario
            log.d("Sn= " + v.getSn());
            mins.add(lim.getSn()); // Límite natural lado contrario
            log.d("LimitSn= " + lim.getSn());

            n = Time.findMajorValue(mins);
            if (n != null)
                log.d("found Sx min= " + n + " (major of above)");

            // limit X
            log.d("Finding Max...");

            List<Time> maxs = new ArrayList<>();
            maxs.add(lim.getFx()); // Límite opuesto
            log.d("LimitFx= " + lim.getFx());
            maxs.add(v.getFx()); // Var opuesta
            log.d("Fx= " + v.getFx());
//            maxs.add(lim.getFn()); // Límite opuesto lado contrario
//            log.d("LimitFn= " + lim.getFn());
//            maxs.add(v.getFn()); // Var opuesta lado contrario
//            log.d("Fn= " + v.getFn());
            if (v.getDn() != null) { // Variable lado contrario afectante: Dn
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
//                if (lim.getFn() != null) {
//                    Time t = Time.sub(lim.getFn(), v.getDn());
//                    maxs.add(t); // límite vecino
//                    log.d("limitFn-Dn= " + t);
//                }
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

        // Se calculan posibles variables de conf derivadas
        ac = new Activity(ac);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n = null, x;

        v.setDn(null);
        if (!isFlexible) v.setDx(null);
        calcActivityVars(ac, K.CONF);

        if (v.getDn() != null) {
            n = Time.copy(v.getDn());
            x = Time.copy(v.getDn());
            log.i("Dn determined");
        } else {
            // limit S
            log.d("Finding Min...");
            // nothing

            // limit X
            log.d("Finding Max...");
            List<Time> maxs = new ArrayList<>();
            maxs.add(v.getDx());
            log.d("Dx= " + v.getDx());

            if (v.getSx() != null) {
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), v.getSx());
                    maxs.add(t);
                    log.d("limitFx-Sx= " + t);
                }
            }
            if (lim.getSx() != null) {
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
                if (lim.getFn() != null) {
                    Time t = Time.sub(lim.getFn(), v.getSn());
                    maxs.add(t);
                    log.d("limitFn-Sn= " + t);
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
            log.d("Dx= " + v.getDx());

            x = Time.findMinorValue(maxs);
            if (x != null) log.d("found Dn max= " + x + " (minor of above)");
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);
        fillEmptyMinMax(minMax);
        log.i("END calcMinMaxDn(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxDx(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxDx(). activity= " + ac + ", isFlexible= " + isFlexible);

        // Se calculan posibles variables de conf derivadas
        ac = new Activity(ac);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n = null, x;

        v.setDx(null);
        if (!isFlexible) v.setDn(null);
        calcActivityVars(ac, K.CONF);

        if (v.getDx() != null) {
            n = Time.copy(v.getDx());
            x = Time.copy(v.getDx());
            log.i("Dx determined");
        } else {

            // limit S
            log.d("Finding Min...");
            if(v.getDn()!=null){
                n = Time.copy(v.getDn());
                log.d("Dn= " + v.getDn());
                log.d("found Dx min= (Dn)" + v.getDn());
            }
//            List<Time> mins = new ArrayList<>();
//            mins.add(v.getDn());// Variable opuesta
//            if (v.getFx() != null) {
//                if (v.getFn() != null) {
//                    Time t = Time.sub(v.getFx(), v.getFn());
//                    t = Time.sum(t, v.getDn());
//                    mins.add(t);
//                    log.d("Fx-Fn+Dn= " + t);
//                }
//                if (lim.getFn() != null) {
//                    Time t = Time.sub(v.getFx(), lim.getFn());
//                    t = Time.sum(t, v.getDn());
//                    if (!t.isNegative()) {
//                        mins.add(t);
//                        log.d("Fx-limitFn+Dn= " + t);
//                    }
//                }
//            }
//            else if (lim.getFx() != null) {
//                if (v.getFn() != null) {
//                    Time t = Time.sub(lim.getFx(), v.getFn());
//                    t = Time.sum(t, v.getDn());
//                    mins.add(t);
//                    log.d("limitFx-Fn+Dn= " + t);
//                }
//                if (lim.getFn() != null) {
//                    Time t = Time.sub(lim.getFx(), lim.getFn());
//                    t = Time.sum(t, v.getDn());
//                    if (!t.isNegative()) {
//                        mins.add(t);
//                        log.d("limitFx-limitFn+Dn= " + t);
//                    }
//                }
//            }

//            if (v.getSx() != null) {
//                if (v.getSn() != null) {
//                    Time t = Time.sub(v.getSx(), v.getSn());
//                    t = Time.sum(t, v.getDn());
//                    mins.add(t);
//                    log.d("Sx-Sn+Dn= " + t);
//                }
//                if (lim.getSn() != null) {
//                    Time t = Time.sub(v.getSx(), lim.getSn());
//                    t = Time.sum(t, v.getDn());
//                    if (!t.isNegative()) {
//                        mins.add(t);
//                        log.d("Sx-limitSn+Dn= " + t);
//                    }
//                }
//            }
//            else if (lim.getSx() != null) {
//                if (v.getSn() != null) {
//                    Time t = Time.sub(lim.getSx(), v.getSn());
//                    t = Time.sum(t, v.getDn());
//                    mins.add(t);
//                    log.d("limitSx-Sn+Dn= " + t);
//                }
//                if (lim.getSn() != null) {
//                    Time t = Time.sub(lim.getSx(), lim.getSn());
//                    t = Time.sum(t, v.getDn());
//                    if (!t.isNegative()) {
//                        mins.add(t);
//                        log.d("limitSx-limitSn+Dn= " + t);
//                    }
//
//                }
//            }
//            n = Time.findMajorValue(mins);
//            if (n != null) log.d("found Fn min= " + n + " (major of above)");

            // limit X
            log.d("Finding Max...");
            List<Time> maxs = new ArrayList<>();

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

            if (v.getSx() != null) {
                if (lim.getFx() != null) {
                    Time t = Time.sub(lim.getFx(), v.getSx());
                    maxs.add(t);
                    log.d("limitFx-Sx= " + t);
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
        fillEmptyMinMax(minMax);
        log.i("END calcMinMaxDx(). minMax= " + minMax);
        return minMax;
    }

    public NX calcMinMaxFn(Activity ac, boolean isFlexible) {
        log.i("BEGIN calcMinMaxFn(). activity= " + ac + ", isFlexible= " + isFlexible);

        // Se calculan posibles variables de conf derivadas
        ac = new Activity(ac);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n, x;

        v.setFn(null);
        if (!isFlexible) v.setFx(null);
        calcActivityVars(ac, K.CONF);

        if (v.getFn() != null) {
            n = Time.copy(v.getFn());
            x = Time.copy(v.getFn());
            log.i("Fn determined");
        } else {
            // limit S
            log.d("Finding Min...");

            List<Time> mins = new ArrayList<>();
            mins.add(v.getSn());// Variable opuesta
            log.d("Sn= " + v.getSn());
            mins.add(lim.getSn());// Límite opuesto
            log.d("limitSn= " + lim.getSn());
            if (v.getDn() != null) { // Otra variable afectante: Dn
                if (lim.getSn() != null) { // ??
                    Time t = Time.sum(lim.getSn(), v.getDn());
                    mins.add(t);
                    log.d("limitSn+Dn= " + t);
                }
            }

            n = Time.findMajorValue(mins);
            if (n != null) log.d("found Fn min= " + n + " (major of above)");

            // limit X
            log.d("Finding Max...");
            List<Time> maxs = new ArrayList<>();

            maxs.add(lim.getFn()); // Límite vecino
            log.d("limitFn= " + lim.getFn());
            maxs.add(v.getFx()); // Gemela lado contrario
            log.d("Fx= " + v.getFx());
            maxs.add(lim.getFx()); // Límite vecino contrario
            log.d("limitFx= " + lim.getFx());

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

        // Se calculan posibles variables de conf derivadas
        ac = new Activity(ac);
        ActivityVars v = ac.getConfigVars();
        Limits lim = ac.getLimits();
        Time n, x = null;

        v.setFx(null);
        if (!isFlexible) v.setFn(null);
        calcActivityVars(ac, K.CONF);

        if (v.getFx() != null) {
            n = Time.copy(v.getFx());
            x = Time.copy(v.getFx());
            log.i("Fn determined");
        } else {
            // LIMIT S
            log.d("Finding Min...");
            List<Time> mins = new ArrayList<>();

            mins.add(v.getFn());// Gemela lado contrario
            log.d("Fn= " + v.getFn());
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
                } else if (lim.getSx() != null) {
                    Time t = Time.sum(lim.getSx(), v.getDn());
                    mins.add(t);
                    log.d("limitSx-Dn= " + t);
                }
                if (v.getSn() != null) {
                    Time t = Time.sum(v.getSn(), v.getDn());
                    mins.add(t);
                    log.d("Sn+Dn= " + t);
                } else if (lim.getSn() != null) { // ??
                    Time t = Time.sum(lim.getSn(), v.getDn());
                    mins.add(t);
                    log.d("limitSn+Dn= " + t);
                }
            }
            if (v.getDx() != null) { // Otra variable afectante: Dx
                if (v.getSn() != null) {
                    Time t = Time.sum(v.getSn(), v.getDx());
                    mins.add(t);
                    log.d("Sn+Dx= " + t);

                } else if (lim.getSn() != null) {
                    Time t = Time.sum(lim.getSn(), v.getDx());
                    mins.add(t);
                    log.d("limitSn+Dx= " + t);
                }
                if (lim.getSx() != null) {
                    Time t = Time.sum(lim.getSx(), v.getDx());
                    mins.add(t);
                    log.d("limitSx+Dx= " + t);
                }
            }

            n = Time.findMajorValue(mins);
            if (n != null) log.d("found Fx min= " + n + " (major of above)");

            // LIMIT X
            log.d("Finding Max...");
            if (lim.getFx() != null) {
                log.d("found Fx max= " + x + "= (limitFx)" + lim.getFx());
                x = Time.copy(lim.getFx());
            }
        }

        NX minMax = new NX();
        minMax.setN(n);
        minMax.setX(x);
        log.i("END calcMinMaxFn(). minMax= " + minMax);
        return minMax;
    }

    public void fillEmptyLimits(Limits l) {
        if (l.getSx() == null) l.setSx(Time.copy(scheStart));
        if (l.getFn() == null) l.setFn(Time.copy(scheFin));

        if (l.getSn() == null) l.setSn(Time.copy(scheStart));
        else if (l.getSx().lessThan(l.getSn())) l.setSx(Time.copy(l.getSn()));

        if (l.getFx() == null) l.setFx(Time.copy(scheFin));
        else if (l.getFn().greaterThan(l.getFx())) l.setFn(Time.copy(l.getFx()));
    }

    public void fillEmptyMinMax(NX minMax){
        if(minMax.getN() == null) minMax.setN(scheStart);
        if(minMax.getX() == null) minMax.setX(scheFin);
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

    public NX calcNoFlexibleMinMax(String var, Activity ac) {
        log.i("BEGIN calcNoFlexibleMinMaxS(). var= " + var + ", activity= " + ac);
        NX minMax = new NX();

        NX min, max;
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
     * Comprueba si una variable de una actividad puede ser establecida como no
     * flexible, es
     * decir, que sus variables máximas y mínimas sean iguales y se traten como
     * una sola.
     *
     * <p> Para ello, comprueba que los valores de validación de una variable
     * sean compatibles entre
     * sus lados máximo y mínimo. En concreto, se comprueba si alguno de los
     * extremos de los
     * valores de validación de las variable del lado mínimo se encuentra dentro
     * del rango de
     * los valores de valiación del lado máximo.
     *
     * @param var      la variable de la actividad que se desea evaluar (
     *                 {@link K#S},
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
            if (sideN.getN() != null && sideN.getN().isInRange(sideX.getN(),
                    sideX.getX())) {
                log.d(var + ": (sideXMin)" + sideX.getN() + " <= (sideNMin)" + sideN.getN() + " " +
                        "<= " +
                        "(sideXMax)" + sideX.getX());
                can = true;
            } else if (sideN.getX() != null && sideN.getX().isInRange(sideX.getN(),
                    sideX.getX())) {
                log.d(var + ": (sideXMin)" + sideX.getN() + " <= (sideNMax)" + sideN.getX() + " " +
                        "<= " +
                        "(sideXMax)" + sideX.getX());
                can = true;
            }
        }

        log.i("END validateDisableFlexible(). var= " + var + ", " +
                "validateDisableFlexible= " + can);
        return can;
    }


}

