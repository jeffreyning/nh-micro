package com.nh.micro.nhs.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {
    /**
     * @param date
     * @param pattern
     * @return Date
     */
    public static Date parse(String date, String pattern) {
        if(date == null) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            return dateFormat.parse(date);
        }
        catch(ParseException e) {
        }
        return null;
    }

    /**
     * @param timeMillis
     * @param pattern
     * @return String
     */
    public static String format(long timeMillis, String pattern) {
        if(timeMillis > 0L) {
            DateFormat dateFormat = new SimpleDateFormat(pattern);
            return dateFormat.format(new Date(timeMillis));
        }
        return "";
    }

    /**
     * @param date
     * @param pattern
     * @return String
     */
    public static String format(Date date, String pattern) {
        if(date == null) {
            return "";
        }

        DateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * @param date
     * @return int
     */
    public static int year(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * @param date
     * @return int
     */
    public static int month(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * @param date
     * @return int
     */
    public static int day(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param date
     * @return String
     */
    public static String smart(Date date) {
        return DateUtil.smart(new Date(System.currentTimeMillis()), date);
    }

    /**
     * @param offsetDate
     * @param date
     * @return String
     */
    public static String smart(Date offsetDate, Date date) {
        if(date == null) {
            return "";
        }

        long offsetTimeMillis = offsetDate.getTime();
        long timeMillis = Math.abs(offsetTimeMillis - date.getTime());
        long hour = 60L * 60L * 1000L;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String result = null;

        if(timeMillis < 60L * 1000L) {
            result = "\u521a\u521a";
        }
        else if(timeMillis < hour) {
            result = (timeMillis / (60L * 1000L)) + " \u5206\u949f\u4ee5\u524d";
        }
        else if(timeMillis < 24 * hour) {
            result = (timeMillis / (60L * 60L * 1000L)) + " \u5c0f\u65f6\u4ee5\u524d";
        }
        else if(timeMillis < 30 * 24 * hour) {
            long days = Math.abs(offsetTimeMillis / 86400000L - date.getTime() / 86400000L);

            if(days < 2) {
                result = "\u6628\u5929  " + dateFormat.format(date);
            }
            else if(days < 3) {
                result = "\u524d\u5929  " + dateFormat.format(date);
            }
            else {
                result = days + " \u5929\u4ee5\u524d";
            }
        }
        else {
            Calendar c1 = Calendar.getInstance();
            c1.setTimeInMillis(offsetTimeMillis);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(date);

            int y1 = c1.get(Calendar.YEAR);
            int y2 = c2.get(Calendar.YEAR);
            int m1 = c1.get(Calendar.MONTH);
            int m2 = c2.get(Calendar.MONTH);
            int d1 = (y1 * 12 + m1);
            int d2 = (y2 * 12 + m2);

            if((d1 - d2) < 12) {
                result = (d1 - d2) + " \u6708\u4ee5\u524d";
            }
            else {
                result = (y1 - y2) + " \u5e74\u4ee5\u524d";
            }
        }
        return result;
    }
}
