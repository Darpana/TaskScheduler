package com.task.scheduler.util;

import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    public static int getDayOfMonth(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DAY_OF_WEEK);
    }

    public static Date addMinutes(Date date, int numberOfMinutes) {
        if (date == null) date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, numberOfMinutes);
        return calendar.getTime();
    }
}
