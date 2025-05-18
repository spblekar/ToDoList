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
    }

    public static class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
    }
}