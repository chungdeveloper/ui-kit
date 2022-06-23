package com.chungld.uipack.calendar.calendar;

import static java.lang.Math.PI;
import static java.lang.Math.floor;
import static java.lang.Math.sin;

public class LunarCalendarVietnamese {
    public static int jdFromDate(int dd, int mm, int yy) {
        double a = floor((14.0f - mm) / 12);
        double y = yy + 4800 - a;
        double m = mm + 12 * a - 3;
        double v = dd + floor((153 * m + 2) / 5) + 365 * y + floor(y / 4);
        double jd = v - floor(y / 100) + floor(y / 400) - 32045;
        if (jd < 2299161) {
            jd = v - 32083;
        }
        return (int) floor(jd);
    }

    @SuppressWarnings("unused")
    public static int[] jdToDate(int jd) {
        int a;
        double b;
        double c;
        if (jd > 2299160) {
            a = jd + 32044;
            b = floor((4.0 * a + 3) / 146097);
            c = a - floor((b * 146097.0) / 4);
        } else {
            b = 0;
            c = jd + 32082;
        }
        double d = floor((4.0 * c + 3) / 1461);
        double e = c - floor((1461 * d) / 4);
        double m = floor((5 * e + 2) / 153);
        double day = e - floor((153 * m + 2) / 5) + 1;
        double month = m + 3 - 12 * floor(m / 10);
        double year = b * 100 + d - 4800 + floor(m / 10);
        return new int[]{(int) floor(day), (int) floor(month), (int) floor(year)};
    }

    public static int getNewMoonDay(int k, int timeZone) {
        double t = k / 1236.85;
        double t2 = t * t;
        double t3 = t2 * t;
        double dr = PI / 180;
        double jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * t2 - 0.000000155 * t3;
        jd1 += 0.00033 * sin((166.56 + 132.87 * t - 0.009173 * t2) * dr); // Mean new moon
        double m = 359.2242 + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3; // Sun's mean anomaly
        double mpr = 306.0253 + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3; // Moon's mean anomaly
        double f =
                21.2964 + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3; // Moon's argument of latitude
        double c1 = (0.1734 - 0.000393 * t) * sin(m * dr) + 0.0021 * sin(2 * dr * m);
        c1 = c1 - 0.4068 * sin(mpr * dr) + 0.0161 * sin(dr * 2 * mpr);
        c1 -= 0.0004 * sin(dr * 3 * mpr);
        c1 = c1 + 0.0104 * sin(dr * 2 * f) - 0.0051 * sin(dr * (m + mpr));
        c1 = c1 - 0.0074 * sin(dr * (m - mpr)) + 0.0004 * sin(dr * (2 * f + m));
        c1 = c1 - 0.0004 * sin(dr * (2 * f - m)) - 0.0006 * sin(dr * (2 * f + mpr));
        c1 += 0.0010 * sin(dr * (2 * f - mpr)) + 0.0005 * sin(dr * (2 * mpr + m));
        double deltaT = (t < -11) ?
                0.001 + 0.000839 * t + 0.0002261 * t2 - 0.00000845 * t3 - 0.000000081 * t * t3
                : -0.000278 + 0.000265 * t + 0.000262 * t2;

        double jdNew = jd1 + c1 - deltaT;
        return (int) floor(jdNew + 0.5 + timeZone / 24.0);
    }

    public static int getSunLongitude(int jdn, int timeZone) {
        double t = (jdn - 2451545.5 - timeZone / 24.0) / 36525;// Time in Julian centuries from 2000-01-01 12:00:00 GMT
        double t2 = t * t;
        double dr = PI / 180; // degree to radian
        double m = 357.52910 + 35999.05030 * t - 0.0001559 * t2 - 0.00000048 * t * t2;// mean anomaly, degree
        double l0 = 280.46645 + 36000.76983 * t + 0.0003032 * t2; // mean longitude, degree
        double dl = (1.914600 - 0.004817 * t - 0.000014 * t2) * sin(dr * m);
        dl += (0.019993 - 0.000101 * t) * sin(dr * 2 * m) + 0.000290 * sin(dr * 3 * m);
        double l = l0 + dl; // true longitude, degree
        l *= dr;
        l -= PI * 2 * floor(l / (PI * 2)); // Normalize to (0, 2*PI)
        return (int) floor(l / PI * 6);
    }

    public static int getLunarMonth11(int yy, int timeZone) {
        double off = jdFromDate(31, 12, yy) - 2415021;
        int k = (int) floor(off / 29.530588853);
        int nm = getNewMoonDay(k, timeZone);
        double sunLong = getSunLongitude(nm, timeZone); // sun longitude at local midnight
        if (sunLong >= 9) {
            nm = getNewMoonDay(k - 1, timeZone);
        }
        return nm;
    }

    public static int getLeapMonthOffset(int a11, int timeZone) {
        int k = (int) floor((a11 - 2415021.076998695) / 29.530588853 + 0.5);
        int last;
        int i = 1; // We start with the month following lunar month 11
        int arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        do {
            last = arc;
            i++;
            arc = getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone);
        } while (arc != last && i < 14);
        return i - 1;
    }

    /**
     * @param dd       - day(dd)
     * @param mm       - month(mm)
     * @param yy       - year(yyyy)
     * @param timeZone - timeZone(z)
     * @return output is int array with values match flow below
     * int[]{dd, mm, yyyy, leap}
     */
    public static int[] convertSolarToLunarDate(int dd, int mm, int yy, int timeZone) {
        int dayNumber = jdFromDate(dd, mm, yy);
        int k = (int) floor((dayNumber - 2415021.076998695) / 29.530588853);
        int monthStart = getNewMoonDay(k + 1, timeZone);
        if (monthStart > dayNumber) {
            monthStart = getNewMoonDay(k, timeZone);
        }
        int a11 = getLunarMonth11(yy, timeZone);
        int b11 = a11;
        int lunarYear;
        int leapMonthDiff;
        if (a11 >= monthStart) {
            lunarYear = yy;
            a11 = getLunarMonth11(yy - 1, timeZone);
        } else {
            lunarYear = yy + 1;
            b11 = getLunarMonth11(yy + 1, timeZone);
        }
        int lunarDay = dayNumber - monthStart + 1;
        int diff = (int) floor((monthStart - a11) / 29.0f);
        int lunarLeap = 0;
        int lunarMonth = diff + 11;
        if (b11 - a11 > 365) {
            leapMonthDiff = getLeapMonthOffset(a11, timeZone);
            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10;
                if (diff == leapMonthDiff) {
                    lunarLeap = 1;
                }
            }
        }
        if (lunarMonth > 12) {
            lunarMonth -= 12;
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1;
        }

        return new int[]{lunarDay, lunarMonth, lunarYear, lunarLeap};
    }

    public static String getLunarText(int[] lunar) {
        return (lunar[0] == 1 ? lunar[0] + "/" + lunar[1] : lunar[0]) + "";
    }
}
