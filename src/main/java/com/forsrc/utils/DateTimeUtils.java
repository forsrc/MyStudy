package com.forsrc.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DateTimeUtils {


    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_TIME = "HH:mm:ss";

    /**
     * @param @return
     * @return String
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime() {
        // Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        // return sdf.format(calendar.getTime());
        return sdf.format(new Date());
    }

    /**
     * @param @param  format
     * @param @return
     * @return String
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(String format) {
        // Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // return sdf.format(calendar.getTime());
        return sdf.format(new Date());
    }

    /**
     * @param @param  date
     * @param @param  format
     * @param @return
     * @return String
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(Date date, String format) {
        // Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // return sdf.format(calendar.getTime());
        return sdf.format(date);
    }

    /**
     * @param date
     * @return String
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(Date date) {
        // Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        // return sdf.format(calendar.getTime());
        return sdf.format(date);
    }

    public static String getDateTime(Date today, int index, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, index);
        Date d = calendar.getTime();
        return getDateTime(d, format);
    }

    /**
     * @param today
     * @param index  　0-6
     * @param format
     * @return String
     * @throws
     * @Title: getWeek
     * @Description:
     */
    public static String getWeek(Date today, int index, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        ArrayList<Date> list = getWeekDateList(today, 0);
        Date d = calendar.getTime();
        for (Date date : list) {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            if (c.get(Calendar.DAY_OF_WEEK) == index + 1) {
                d = c.getTime();
            }
        }

        return getDateTime(d, format);
    }

    /**
     * @param today
     * @param index
     * @return ArrayList<Date>
     * @throws
     * @Title: getDateList
     * @Description:
     */
    public static ArrayList<Date> getDateList(Date today, int index) {
        ArrayList<Date> list = new ArrayList<Date>();
        for (int i = 0; i <= (index < 0 ? 0 - index : index); i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(today);
            calendar.add(Calendar.DAY_OF_MONTH, index < 0 ? 0 - i : i);
            Date d = calendar.getTime();
            list.add(d);
        }
        if (list.size() == 0) {
            list.add(today);
        }

        return list;
    }

    public static String getDateListReg(Date today, int index, String format) {
        return getDatesReg(getDateList(today, index), format);
    }

    /**
     * @param today
     * @param index
     * @return ArrayList<Date>
     * @throws
     * @Title: getWeekDateList
     * @Description:
     */
    public static ArrayList<Date> getWeekDateList(Date today, int index) {
        ArrayList<Date> list = new ArrayList<Date>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.WEEK_OF_YEAR, -1);
        }
        calendar.add(Calendar.WEEK_OF_YEAR, index);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        for (int i = 0; i <= 6; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(calendar.getTime());
            c.add(Calendar.DAY_OF_MONTH, i);
            Date d = c.getTime();
            list.add(d);
        }
        if (list.size() == 0) {
            list.add(today);
        }
        return list;
    }

    public static String getWeekDateListReg(Date today, int index, String format) {
        ArrayList<Date> list = DateTimeUtils.getWeekDateList(today, index);
        return getDatesReg(list, format);
    }

    public static String getDatesReg(ArrayList<Date> list, String format) {
        Iterator<Date> it = list.iterator();
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (it.hasNext()) {
            Date d = it.next();
            if (i++ == 0) {
                sb.append("(");
            } else {
                sb.append("|(");
            }
            sb.append(DateTimeUtils.getDateTime(d, format)).append(")");
        }
        return sb.toString();
    }

    /**
     * @param @param  currentTimeMillis
     * @param @param  format
     * @param @return
     * @return String
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(long currentTimeMillis, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(currentTimeMillis));
    }

    public static Date stringToDate(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param @param  currentTimeMillis
     * @param @return
     * @return String
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(long currentTimeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        return sdf.format(new Date(currentTimeMillis));
    }

    /**
     * @param currentTimeMillis
     * @param ms
     * @return String
     * @throws
     * @Title: getDateTime
     * @Description:
     */
    public static String getDateTime(long currentTimeMillis, boolean ms) {
        String format = ms ? "yyyy-MM-dd HH:mm:ss.SSS" : FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(currentTimeMillis));
    }


    public static Date format(String format, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            throw e;
        }
    }

    public static Date parse(String format, String date) throws ParseException {
        if (date == null) {
            return null;
        }
        Pattern pattern = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2} \\d{2}\\:\\d{2}\\:\\d{2}$");
        Matcher matcher = pattern.matcher(date);

        if (matcher.matches()) {
            return DateTimeUtils.format(FORMAT_DATE_TIME, date);
        }

        pattern = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2}$");
        matcher = pattern.matcher(date);

        if (matcher.matches()) {
            return DateTimeUtils.format(FORMAT_DATE, date);
        }

        pattern = Pattern.compile("^\\d{2}\\:\\d{2}\\:\\d{2}$");
        matcher = pattern.matcher(date);

        if (matcher.matches()) {
            return DateTimeUtils.format(FORMAT_TIME, date);
        }

        pattern = Pattern.compile("^\\d+$");
        matcher = pattern.matcher(date);

        Date d = null;
        if (matcher.matches()) {
            long time = Long.parseLong(date);
            d = new Date(time);
            return d;
        }
        throw new ParseException(date, 0);
    }

    public static Date parse(String date) throws ParseException {
        return parse(FORMAT, date);
    }
}
