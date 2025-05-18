package com.example.rasklad.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCategoriesTable = "CREATE TABLE " + TasksContract.CategoryEntry.TABLE_NAME + " (" +
                TasksContract.CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TasksContract.CategoryEntry.COLUMN_NAME + " TEXT NOT NULL)";
        String createTasksTable = "CREATE TABLE " + TasksContract.TaskEntry.TABLE_NAME + " (" +
                TasksContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TasksContract.TaskEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TasksContract.TaskEntry.COLUMN_DESCRIPTION + " TEXT, " +
                TasksContract.TaskEntry.COLUMN_DUE_DATE + " INTEGER, " +
                TasksContract.TaskEntry.COLUMN_CATEGORY_ID + " INTEGER, " +
                TasksContract.TaskEntry.COLUMN_PRIORITY + " INTEGER, " +
                TasksContract.TaskEntry.COLUMN_COMPLETED + " INTEGER, " +
                "FOREIGN KEY(" + TasksContract.TaskEntry.COLUMN_CATEGORY_ID + ") REFERENCES " +
                TasksContract.CategoryEntry.TABLE_NAME + "(" + TasksContract.CategoryEntry._ID + "))";
        db.execSQL(createCategoriesTable);
        db.execSQL(createTasksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TasksContract.CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TasksContract.TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}