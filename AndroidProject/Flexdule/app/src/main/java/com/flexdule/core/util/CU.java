package com.flexdule.core.util;

import java.time.Duration;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase de útils pertenecientes al Core
 */
public class CU {

    /**
     * Convierte un string que reprente una duracíón en formato HH:MM a la clase Duration
     * <p>
     * Admite horas negativas (-HH:MM)
     *
     * @param hour la hora en formato HH:MM
     * @return la hora convertida a <code>Duration</code>
     * @throws IllegalArgumentException
     */
    public static Duration hourToDur(String hour) throws IllegalArgumentException {
        Duration d = null;

        if (hour != null) {
            try {
                hour.replaceAll("\\s+", "");
                boolean negative = hour.charAt(0) == '-';
                if (negative) hour = hour.substring(1);
                String[] parts = hour.split(":");

                long h = Integer.parseInt(parts[0]);
                long m = Integer.parseInt(parts[1]);
                long s = 0;
                if (parts.length == 3) {
                    s = Integer.parseInt(parts[2]);
                }

                s += m * 60;
                s += h * 3600;
                if (s < 0) throw new IllegalArgumentException();
                if (negative) s *= -1;
                d = Duration.ofSeconds(s);

            } catch (Exception e) {
                throw new IllegalArgumentException();
            }
        }
        return d;
    }

    /**
     * Convierte un objeto Duration a una cadena de texto en formato HH:MM.
     *
     * @param duration una duración.
     * @return la duración en formato HH:MM.
     */
    public static String durToHour(Duration duration) {
        String str = null;

        if (duration != null) {
            long d = duration.toMinutes();
            boolean negative = duration.isNegative();
            if (negative) {
                d *= -1;
            }

            long h = d / 60;
            String hS = String.valueOf(h);
            if (h < 10) {
                hS = "0" + hS;
            }

            long m = d % 60;
            String mS = String.valueOf(m);
            if (m < 10) {
                mS = "0" + mS;
            }

            str = hS + ":" + mS;
            if (negative) {
                str = "-" + str;
            }
        }
        return str;
    }

    /**
     * Convierte un string que represente una duración en formato "Xh Xm Xs" a un objeto de clase Duration.
     * <p>
     * Se admite un signo "-" delante para duraciones negativas.<br>
     * Pueden no indicarse algún componente de la duración, pero debe haber al menos uno. <br>
     * Ejemplos de formatos válidos: "1h", "-1h", "-1h 5s".
     *
     * @param string la duración en formato "Xh Xm Xs".
     * @return la duración como objeto Duration.
     * @throws IllegalArgumentException
     */
    public static Duration stringToDur(String string) throws IllegalArgumentException {
        Duration d = null;

        if (string != null) {
            try {
                string = string.trim().toLowerCase();
                Pattern pattern = Pattern.compile("\\d+[hms]");
                Matcher matcher = pattern.matcher(string);

                boolean negative = string.charAt(0) == '-';
                long h = 0, m = 0, s = 0;

                int i = 0;
                while (matcher.find()) {
                    i++;

                    String g = matcher.group();
                    switch (g.charAt(g.length() - 1)) {
                        case 'h':
                            String hS = g.split("h")[0];
                            h = Long.parseLong(hS);
                            break;
                        case 'm':
                            String mS = g.split("m")[0];
                            m = Long.parseLong(mS);
                            break;
                        case 's':
                            String sS = g.split("s")[0];
                            s = Long.parseLong(sS);
                            break;
                    }
                }
                if (i < 1) {
                    throw new IllegalArgumentException();
                }

                s += m * 60;
                s += h * 3600;
                if (negative) s *= -1;
                d = Duration.ofSeconds(s);

            } catch (Exception e) {
                System.out.println(e);
                throw new IllegalArgumentException();
            }
        }
        return d;
    }

    /**
     * Convierte un objeto duración a un string con formato "Xh Xm Xs".
     *
     * @param duration el objeto duración a convertir.
     * @return la duración en formato "Xh Xm Xs".
     */
    public static String durToString(Duration duration) {
        String str = null;
        if (duration != null) {
            long d = duration.toMillis();
            boolean negative = duration.isNegative();
            d /= 1000;
            if (negative) {
                d *= -1;
            }
            long h = d / 3600;
            long m = (d % 3600) / 60;
            long s = d % 60;

            str = "";
            if (h != 0) {
                str += h + "h ";
            }
            if (h != 0 || m != 0) {
                str += m + "m ";
            }
            str += s + "s";

            if (negative) {
                str = "-" + str;
            }
        }
        return str;
    }

    public static Integer durHours(Duration duration) {
        Integer result = null;
        if (duration != null) {
            long d = duration.toMillis()/1000;
            if (duration.isNegative()) {
                d *= -1;
            }
            result = (int) (d / 3600);
        }
        return result;
    }

    public static Integer durMinutes(Duration duration) {
        Integer result = null;
        if (duration != null) {
            long d = duration.toMillis()/1000;
            if (duration.isNegative()) {
                d *= -1;
            }
            result = (int) ((d % 3600) / 60);
        }
        return result;
    }

    public static Integer durSeconds(Duration duration) {
        Integer result = null;
        if (duration != null) {
            long d = duration.toMillis()/1000;
            if (duration.isNegative()) {
                d *= -1;
            }
            result = (int) (d % 60);
        }
        return result;
    }


    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
