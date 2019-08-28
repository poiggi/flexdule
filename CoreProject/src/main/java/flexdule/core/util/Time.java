package flexdule.core.util;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representa tiempo en horas, minutos y segundos.
 */
public class Time implements Comparable<Time>, Serializable {

    /**
     * Duración total en segundos
     */
    protected int s;

    public Time() {
    }

    public Time(int seconds) {
        this.s = seconds;
    }

    public Time(int h, int m) {
        add(h, m, 0);
    }

    public Time(int h, int m, int s) {
        add(h, m, s);
    }

    public Time(String hour) {
        Time t = Time.parseHour(hour);
        s = t.asSeconds();
    }

    public static Time copy(Time time) {
        Time r = null;
        if (time != null) {
            r = new Time(time.asSeconds());
        }
        return r;
    }

    public void copyValue(Time time) {
        if (time != null)
            this.s = time.asSeconds();
        else
            this.s = 0;
    }

    public void addSeconds(int s) {
        this.s += s;
    }

    public void addMinutes(int m) {
        s += m * 60;
    }

    public void addHours(int h) {
        s += h * 3600;
    }

    public void add(int h, int m, int s) {
        addSeconds(s);
        addMinutes(m);
        addHours(h);
    }

    public void toZero() {
        s = 0;
    }

    public int getHours() {
        return s / 3600;
    }

    public int getMinutes() {
        return (s % 3600) / 60;
    }

    public int getSeconds() {
        return s % 60;
    }

    public int asSeconds() {
        return s;
    }

    public boolean isNegative() {
        return s < 0;
    }

    public Time negate() {
        s *= -1;
        return this;
    }

    public Time plus(Time t) {
        if (t != null)
            s += t.asSeconds();
        return this;
    }

    public Time minus(Time t) {
        if (t != null)
            s -= t.asSeconds();
        return this;
    }

    public static Time sum(Time t1, Time t2) {
        Time t = new Time();
        t.plus(t1);
        t.plus(t2);
        return t;
    }

    public static Time sub(Time t1, Time t2) {
        Time t = new Time();
        t.plus(t1);
        t.minus(t2);
        return t;
    }


    /**
     * Convierte el objeto a una cadena de texto en formato HH:MM:SS.
     * <br> Los segundos solo se indican si su valor no es cero.
     *
     * @return el tiempo en formato HH:MM:SS.
     */
    protected String formatHour() {
        String str = null;

        boolean negative = isNegative();

        int h = Math.abs(getHours());
        String hS = String.valueOf(h);
        if (h < 10) {
            hS = "0" + hS;
        }

        int m = Math.abs(getMinutes());
        String mS = String.valueOf(m);
        if (m < 10) {
            mS = "0" + mS;
        }

        int s = Math.abs(getSeconds());
        String sS = "";
        if (s != 0) {
            sS = String.valueOf(s);
            if (s < 10) {
                sS = "0" + sS;
            }
            sS = ":" + sS;
        }

        str = hS + ":" + mS + sS;

        if (negative) {
            str = "-" + str;
        }

        return str;
    }

    /**
     * Convierte un string que en formato HH:MM[:SS] a la clase Time
     * <p>
     * Admite formatos negativos (-HH:MM:SS)
     *
     * @param hour la hora en formato HH:MM
     * @return la hora convertida a <code>Time</code>
     * @throws IllegalArgumentException
     */
    protected static Time parseHour(String hour) throws IllegalArgumentException {
        Time t = null;

        if (hour != null) {
            try {
//                hour.replaceAll("\\s+", "");
                boolean negative = hour.charAt(0) == '-';
                if (negative) hour = hour.substring(1);

                String[] parts = hour.split(":");
                int h = Integer.parseInt(parts[0]);
                int m = Integer.parseInt(parts[1]);
                int s = 0;
                if (parts.length == 3) {
                    s = Integer.parseInt(parts[2]);
                }

                if (h < 0 || m < 0 || s < 0) throw new IllegalArgumentException();
                t = new Time(h, m, s);
                if (negative) t.negate();

            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        }
        return t;
    }


    public String formatText() {
        return formatText(true, false);
    }

    public String formatText(boolean spaces, boolean upper) {
        String str = null;
        boolean negative = isNegative();

        int h = Math.abs(getHours());
        int m = Math.abs(getMinutes());
        int s = Math.abs(getSeconds());

        str = "";
        if (h != 0) {
            str += h + "h";
            if (spaces) str += " ";
        }

        if (h != 0 || m != 0)
            str += m + "m";

        if (str.equals("") || s != 0) {
            if (spaces && (h != 0 || m != 0))
                str += " ";
            str += s + "s";
        }

        if (negative)
            str = "-" + str;

        if (upper)
            str = str.toUpperCase();

        return str;
    }

    /**
     * Convierte un string que represente tiempo en formato "Xh Xm Xs" a un objeto de clase Time.
     * <p>
     * Se admite un signo "-" delante para duraciones negativas.<br>
     * Pueden no indicarse algún componente temporal, pero debe haber al menos uno.<br>
     * Ejemplos de formatos válidos: "1h", "-1h", "-1h 5s".
     *
     * @param string la duración en formato "Xh Xm Xs".
     * @return la duración como objeto Duration.
     * @throws IllegalArgumentException
     */
    public static Time parseText(String string) throws IllegalArgumentException {
        Time t = new Time();

        if (string != null) {
            try {
                string = string.trim().toLowerCase();
                Pattern pattern = Pattern.compile("\\d+[hms]");
                Matcher matcher = pattern.matcher(string);

                boolean negative = string.charAt(0) == '-';

                int i = 0;
                while (matcher.find()) {
                    i++;

                    String g = matcher.group();
                    switch (g.charAt(g.length() - 1)) {
                        case 'h':
                            String hS = g.split("h")[0];
                            t.addHours(Integer.parseInt(hS));
                            break;
                        case 'm':
                            String mS = g.split("m")[0];
                            t.addMinutes(Integer.parseInt(mS));
                            break;
                        case 's':
                            String sS = g.split("s")[0];
                            t.addSeconds(Integer.parseInt(sS));
                            break;
                    }
                }
                if (i < 1) {
                    throw new IllegalArgumentException();
                }

                if (negative) t.negate();

            } catch (Exception e) {
                System.out.println(e);
                throw new IllegalArgumentException();
            }
        }
        return t;
    }


    @Override
    public String toString() {
        return formatHour();
    }

    public static Time parse(String string) {
        return parseHour(string);
    }

    @Override
    public int compareTo(Time time) {
        if (time != null) {
            if (this.asSeconds() > time.asSeconds()) {
                return 1;
            } else if (this.asSeconds() == time.asSeconds()) {
                return 0;
            } else if (this.asSeconds() < time.asSeconds()) {
                return -1;
            }
        }
        return 1;
    }

    public boolean greaterThan(Time time) {
        return this.asSeconds() > time.asSeconds();
    }

    public boolean greaterOrEqualTo(Time time) {
        return this.asSeconds() >= time.asSeconds();
    }

    public boolean lessThan(Time time) {
        return this.asSeconds() < time.asSeconds();
    }

    public boolean lessOrEqualTo(Time time) {
        return this.asSeconds() <= time.asSeconds();
    }

    public boolean isInRange(Time min, Time max) {
        boolean inRange = true;
        if (min != null) inRange = greaterOrEqualTo(min);
        if (max != null) inRange = inRange && lessOrEqualTo(max);
        return inRange;
    }

    /**
     * Devuelve el tiempo mayor.
     * <br> Si solo hay un tiempo no nulo, será considerado el mayor.
     */
    public static Time findMajorValue(Time t1, Time t2) {
        Time minor = null;
        if (t1 != null && t2 != null) {
            if (t1.greaterOrEqualTo(t2)) {
                minor = t1;
            } else {
                minor = t2;
            }
        } else if (t1 != null) {
            minor = t1;
        } else if (t2 != null) {
            minor = t2;
        }
        return Time.copy(minor);
    }

    public static Time findMajorValue(List<Time> times) {
        Time major = null;
        if (times.size() == 1) {
            major = Time.copy(times.get(0));
        } else {
            for (Time time: times) {
                major = findMajorValue(time, major);
            }
        }
        return major;
    }

    /**
     * Devuelve el tiempo menor.
     * <br> Si solo hay un tiempo no nulo, será considerado el menor.
     */
    public static Time findMinorValue(Time t1, Time t2) {
        Time major = null;
        if (t1 != null && t2 != null) {
            if (t1.lessOrEqualTo(t2)) {
                major = t1;
            } else {
                major = t2;
            }
        } else if (t1 != null) {
            major = t1;
        } else if (t2 != null) {
            major = t2;
        }
        return Time.copy(major);
    }

    public static Time findMinorValue(List<Time> times) {
        Time minor = null;
        if (times.size() == 1) {
            minor = Time.copy(times.get(0));
        } else {
            for (Time time: times) {
                minor = findMinorValue(time, minor);
            }
        }
        return minor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Time time = (Time) o;
        return s == time.s;
    }

    @Override
    public int hashCode() {
        return Objects.hash(s);
    }
}
