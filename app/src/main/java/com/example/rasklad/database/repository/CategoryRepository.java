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
        try (Cursor cursor = db.rawQuery(query, new String[]{category.getName(), String.valueOf(category.getId())})) {
            if (cursor != null && cursor.moveToFirst()) {
                return -1;
            }
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

    @SuppressLint("Range")
    public int getOrCreateDefaultCategoryId() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = TasksContract.CategoryEntry.COLUMN_NAME + " = ?";
        String[] args = { DBHelper.DEFAULT_CATEGORY_NAME };
        int id = -1;
        try (Cursor cursor = db.query(TasksContract.CategoryEntry.TABLE_NAME,
                new String[]{TasksContract.CategoryEntry._ID},
                where, args, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                id = cursor.getInt(cursor.getColumnIndex(TasksContract.CategoryEntry._ID));
            } else {
                ContentValues values = new ContentValues();
                values.put(TasksContract.CategoryEntry.COLUMN_NAME, DBHelper.DEFAULT_CATEGORY_NAME);
                id = (int) db.insert(TasksContract.CategoryEntry.TABLE_NAME, null, values);
            }
        }
        return id;
    }

    public boolean isCategoryNameExists(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TasksContract.CategoryEntry.TABLE_NAME
                + " WHERE LOWER(" + TasksContract.CategoryEntry.COLUMN_NAME + ") = LOWER(?)";
        try (Cursor cursor = db.rawQuery(query, new String[]{name.toLowerCase()})) {
            return cursor != null && cursor.moveToFirst() && cursor.getInt(0) > 0;
        }
    }

    @SuppressLint("Range")
    public String getCategoryNameById(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String name = DBHelper.DEFAULT_CATEGORY_NAME;
        try (Cursor cursor = db.query(
                TasksContract.CategoryEntry.TABLE_NAME,
                new String[]{TasksContract.CategoryEntry.COLUMN_NAME},
                TasksContract.CategoryEntry._ID + " = ?",
                new String[]{String.valueOf(categoryId)},
                null, null, null
        )) {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(TasksContract.CategoryEntry.COLUMN_NAME));
            }
        }
        return name;
    }

    @SuppressLint("Range")
    public List<Category> getAllCategories() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Category> categories = new ArrayList<>();
        try (Cursor cursor = db.query(TasksContract.CategoryEntry.TABLE_NAME, null, null, null, null, null, TasksContract.CategoryEntry.COLUMN_NAME + " ASC")) {
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setId(cursor.getInt(cursor.getColumnIndex(TasksContract.CategoryEntry._ID)));
                    category.setName(cursor.getString(cursor.getColumnIndex(TasksContract.CategoryEntry.COLUMN_NAME)));
                    categories.add(category);
                } while (cursor.moveToNext());
            }
        }
        return categories;
    }
}