package com.acenosekai.antplayer.ant;

/**
 * Created by Acenosekai on 1/23/2016.
 * Rock On
 */
public class Utility {
    public static String secondsToString(Integer sec) {
        int h = 0;
        int m = 0;
        if (sec >= 3600) {
            h = (sec - (sec % 3600)) / 3600;
        }
        int hDev = sec - (h * 3600);
        if (hDev >= 60) {
            m = (hDev - (hDev % 60)) / 60;
        }
        int s = hDev - (m * 60);
        return repairDigit(h) + ":" + repairDigit(m) + ":" + repairDigit(s);

    }

    public static String repairDigit(Integer dg) {
        if (dg < 10) {
            return "0" + dg;
        }
        return dg.toString();
    }
}
