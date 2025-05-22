package com.example.rasklad.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String formatDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public static String formatTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    public static String formatFullDate(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd.MM.yyyy", new Locale("ru"));
        return sdf.format(new Date(millis));
    }

    public static boolean isToday(long millis) {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStart = today.getTimeInMillis();
        return (millis >= todayStart && millis < todayStart + 86400000);
    }

    public static String getRelativeDayName(long millis) {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        long today = todayCal.getTimeInMillis();

        long diff = (millis - today) / (24 * 60 * 60 * 1000);

        if (diff == 0) return "Сегодня";
        if (diff == 1) return "Завтра";
        if (diff == 2) return "Послезавтра";

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", new Locale("ru"));
        String dayName = sdf.format(new Date(millis));
        return dayName.substring(0, 1).toUpperCase() + dayName.substring(1);
    }
}