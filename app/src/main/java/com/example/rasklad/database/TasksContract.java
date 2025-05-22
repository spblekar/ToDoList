package com.example.rasklad.database;

import android.provider.BaseColumns;

public final class TasksContract {
    private TasksContract() {}

    public static class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DUE_DATE = "due_date";
        public static final String COLUMN_CATEGORY_ID = "category_id";
        public static final String COLUMN_PRIORITY = "priority";
        public static final String COLUMN_COMPLETED = "completed";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_TITLE + " TEXT, " +
                        COLUMN_DESCRIPTION + " TEXT, " +
                        COLUMN_DUE_DATE + " INTEGER, " +
                        COLUMN_CATEGORY_ID + " INTEGER, " +
                        COLUMN_PRIORITY + " INTEGER, " +
                        COLUMN_COMPLETED + " INTEGER DEFAULT 0, " +
                        "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " +
                        CategoryEntry.TABLE_NAME + "(" + CategoryEntry._ID + ")" +
                        ")";
    }

    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_NAME + " TEXT UNIQUE" +
                        ")";
    }
}