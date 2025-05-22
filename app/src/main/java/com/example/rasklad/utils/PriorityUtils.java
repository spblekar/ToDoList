package com.example.rasklad.utils;

import android.graphics.Color;

public class PriorityUtils {
    public static int getColorForPriority(int priority) {
        switch (priority) {
            case 2: return Color.RED; // Высокий
            case 1: return Color.parseColor("#FFA500"); // Средний
            case 0: return Color.GREEN; // Низкий
            default: return Color.GRAY;
        }
    }
}