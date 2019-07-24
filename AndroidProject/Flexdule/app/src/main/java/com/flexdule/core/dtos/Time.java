package com.flexdule.core.dtos;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Representa tiempo en horas, minutos y segundos.
 */
public class Time implements Comparable<Time> {

    /**
     * Duración total en segundos
     */
    private int s;

    public Time() {
    }

    public Time(int h, int m) {
        add(h, m, 0);
    }

    public Time(int h, int m, int s) {
        add(h, m, s);
    }

    public Time(String hour) {
        Time t = Time.parseHour(hour);
        s = t.getTimeAsSeconds();
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

    public int getTimeAsSeconds() {
        return s;
    }

    public boolean isNegative() {
        return s < 0;
    }

    public void negate() {
        s *= -1;
    }

    /**
     * Convierte el objeto a una cadena de texto en formato HH:MM:SS.
     * <br> Los segundos solo se indican si su valor no es cero.
     *
     * @return el tiempo en formato HH:MM:SS.
     */
    private String formatHour() {
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
    private static Time parseHour(String hour) throws IllegalArgumentException {
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
            if (this.getTimeAsSeconds() > time.getTimeAsSeconds()) {
                return 1;
            } else if (this.getTimeAsSeconds() == time.getTimeAsSeconds()) {
                return 0;
            } else if (this.getTimeAsSeconds() < time.getTimeAsSeconds()) {
                return -1;
            }
        }
        return 1;
    }

    public boolean greaterThan(Time time) {
        return this.getTimeAsSeconds() > time.getTimeAsSeconds();
    }

    public boolean greaterOrEqualTo(Time time) {
        return this.getTimeAsSeconds() >= time.getTimeAsSeconds();
    }

    public boolean lessThan(Time time) {
        return this.getTimeAsSeconds() < time.getTimeAsSeconds();
    }

    public boolean lessOrEqualTo(Time time) {
        return this.getTimeAsSeconds() <= time.getTimeAsSeconds();
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
