package com.example.rasklad.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;
    private static DBHelper instance;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TasksContract.CategoryEntry.SQL_CREATE_TABLE);
        db.execSQL(TasksContract.TaskEntry.SQL_CREATE_TABLE);

        ContentValues defaultCat = new ContentValues();
        defaultCat.put(TasksContract.CategoryEntry.COLUMN_NAME, "Без категории");
        db.insert(TasksContract.CategoryEntry.TABLE_NAME, null, defaultCat);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}