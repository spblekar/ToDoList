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
        dbHelper = DBHelper.getInstance(context);
    }

    public long addCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (isCategoryNameExists(category.getName())) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(TasksContract.CategoryEntry.COLUMN_NAME, category.getName());

        return db.insert(TasksContract.CategoryEntry.TABLE_NAME, null, values);
    }

    public int updateCategory(Category category) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String query = "SELECT _id FROM " + TasksContract.CategoryEntry.TABLE_NAME +
                " WHERE LOWER(" + TasksContract.CategoryEntry.COLUMN_NAME + ") = LOWER(?) " +
                "AND " + TasksContract.CategoryEntry._ID + " != ?";
        Cursor cursor = db.rawQuery(query, new String[]{category.getName(), String.valueOf(category.getId())});

        boolean nameExists = cursor.moveToFirst();
        cursor.close();

        if (nameExists) {
            return -1;
        }

        ContentValues values = new ContentValues();
        values.put(TasksContract.CategoryEntry.COLUMN_NAME, category.getName());
        String where = TasksContract.CategoryEntry._ID + " = ?";
        String[] args = { String.valueOf(category.getId()) };
        return db.update(TasksContract.CategoryEntry.TABLE_NAME, values, where, args);
    }

    public int deleteCategory(int categoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(TasksContract.CategoryEntry.TABLE_NAME,
                TasksContract.CategoryEntry._ID + " = ?",
                new String[]{String.valueOf(categoryId)});
    }

    public int getOrCreateDefaultCategoryId() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = TasksContract.CategoryEntry.COLUMN_NAME + " = ?";
        String[] args = { "Без категории" };
        Cursor cursor = db.query(TasksContract.CategoryEntry.TABLE_NAME, null, where, args, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(TasksContract.CategoryEntry._ID));
            cursor.close();
            return id;
        } else {
            ContentValues values = new ContentValues();
            values.put(TasksContract.CategoryEntry.COLUMN_NAME, "Без категории");
            long id = db.insert(TasksContract.CategoryEntry.TABLE_NAME, null, values);
            return (int) id;
        }
    }

    public boolean isCategoryNameExists(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TasksContract.CategoryEntry.TABLE_NAME
                + " WHERE LOWER(" + TasksContract.CategoryEntry.COLUMN_NAME + ") = LOWER(?)";

        try (Cursor cursor = db.rawQuery(query, new String[]{name})) {
            return cursor.moveToFirst() && cursor.getInt(0) > 0;
        }
    }

    @SuppressLint("Range")
    public String getCategoryNameById(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                TasksContract.CategoryEntry.TABLE_NAME,
                new String[]{TasksContract.CategoryEntry.COLUMN_NAME},
                TasksContract.CategoryEntry._ID + " = ?",
                new String[]{String.valueOf(categoryId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(TasksContract.CategoryEntry.COLUMN_NAME));
            cursor.close();
            return name;
        }
        return "Без категории";
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