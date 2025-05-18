package com.example.rasklad.utils;

import android.graphics.Color;

public class PriorityUtils {
    public static int getColorForPriority(int priority) {
        switch (priority) {
            case 2: return Color.RED; // Высокий - красный
            case 1: return Color.parseColor("#FFA500"); // Средний - оранжевый
            case 0: return Color.GREEN; // Низкий - зеленый
            default: return Color.GRAY;
        }
    }

    public static String getPriorityString(int priority) {
        switch (priority) {
            case 2: return "Высокий";
            case 1: return "Средний";
            case 0: return "Низкий";
            default: return "";
        }
    }
}