package com.example.rasklad.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.rasklad.constants.AppConstants;

public class PreferenceHelper {
    public static boolean isNotificationsEnabled(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(AppConstants.KEY_PREF_NOTIFICATIONS, true);
    }

    public static void setNotificationsEnabled(Context context, boolean enabled) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(AppConstants.KEY_PREF_NOTIFICATIONS, enabled);
        editor.apply();
    }

    public static int getTheme(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(AppConstants.KEY_PREF_THEME, AppConstants.THEME_LIGHT);
    }

    public static void setTheme(Context context, int theme) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(AppConstants.KEY_PREF_THEME, theme);
        editor.apply();
    }

    public static int getSortOrder(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(AppConstants.KEY_PREF_SORT, AppConstants.SORT_BY_DATE);
    }

    public static void setSortOrder(Context context, int sortOrder) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(AppConstants.KEY_PREF_SORT, sortOrder);
        editor.apply();
    }
}