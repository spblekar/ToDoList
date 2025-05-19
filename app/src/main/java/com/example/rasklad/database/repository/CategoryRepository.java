package com.example.rasklad.database.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.rasklad.database.DBHelper;
import com.example.rasklad.database.TasksContract;
import com.example.rasklad.models.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    private DBHelper dbHelper;

    public CategoryRepository(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long addCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksContract.CategoryEntry.COLUMN_NAME, category.getName());
        return db.insert(TasksContract.CategoryEntry.TABLE_NAME, null, values);
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksContract.CategoryEntry.COLUMN_NAME, category.getName());
        String where = TasksContract.CategoryEntry._ID + " = ?";
        String[] args = { String.valueOf(category.getId()) };
        return db.update(TasksContract.CategoryEntry.TABLE_NAME, values, where, args);
    }

    public int deleteCategory(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = TasksContract.CategoryEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        return db.delete(TasksContract.CategoryEntry.TABLE_NAME, where, args);
    }

    @SuppressLint("Range")
    public Category getCategory(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String where = TasksContract.CategoryEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        Cursor cursor = db.query(TasksContract.CategoryEntry.TABLE_NAME, null, where, args, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Category category = new Category();
            category.setId(cursor.getInt(cursor.getColumnIndex(TasksContract.CategoryEntry._ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(TasksContract.CategoryEntry.COLUMN_NAME)));
            cursor.close();
            return category;
        }
        return null;
    }

    @SuppressLint("Range")
    public List<Category> getAllCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TasksContract.CategoryEntry.TABLE_NAME, null, null, null, null, null, null);
        List<Category> categories = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(TasksContract.CategoryEntry._ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(TasksContract.CategoryEntry.COLUMN_NAME)));
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }
}