package com.example.rasklad.database.repository;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.rasklad.database.DBHelper;
import com.example.rasklad.database.TasksContract;
import com.example.rasklad.models.Task;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class TaskRepository {
    private DBHelper dbHelper;
    public TaskRepository(Context context)
    {
        dbHelper = DBHelper.getInstance(context);
    }

    public long addTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksContract.TaskEntry.COLUMN_TITLE, task.getTitle());
        values.put(TasksContract.TaskEntry.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TasksContract.TaskEntry.COLUMN_DUE_DATE, task.getDueDate());
        values.put(TasksContract.TaskEntry.COLUMN_CATEGORY_ID, task.getCategoryId());
        values.put(TasksContract.TaskEntry.COLUMN_PRIORITY, task.getPriority());
        values.put(TasksContract.TaskEntry.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        return db.insert(TasksContract.TaskEntry.TABLE_NAME, null, values);
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksContract.TaskEntry.COLUMN_TITLE, task.getTitle());
        values.put(TasksContract.TaskEntry.COLUMN_DESCRIPTION, task.getDescription());
        values.put(TasksContract.TaskEntry.COLUMN_DUE_DATE, task.getDueDate());
        values.put(TasksContract.TaskEntry.COLUMN_CATEGORY_ID, task.getCategoryId());
        values.put(TasksContract.TaskEntry.COLUMN_PRIORITY, task.getPriority());
        values.put(TasksContract.TaskEntry.COLUMN_COMPLETED, task.isCompleted() ? 1 : 0);
        String where = TasksContract.TaskEntry._ID + " = ?";
        String[] args = { String.valueOf(task.getId()) };
        return db.update(TasksContract.TaskEntry.TABLE_NAME, values, where, args);
    }

    public int deleteTask(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String where = TasksContract.TaskEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        return db.delete(TasksContract.TaskEntry.TABLE_NAME, where, args);
    }

    @SuppressLint("Range")
    public Task getTask(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String where = TasksContract.TaskEntry._ID + " = ?";
        String[] args = { String.valueOf(id) };
        Cursor cursor = db.query(TasksContract.TaskEntry.TABLE_NAME, null, where, args, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Task task = new Task();
            task.setId(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry._ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_DESCRIPTION)));
            task.setDueDate(cursor.getLong(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_DUE_DATE)));
            task.setCategoryId(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_CATEGORY_ID)));
            task.setPriority(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_PRIORITY)));
            task.setCompleted(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_COMPLETED)) == 1);
            cursor.close();
            return task;
        }
        return null;
    }

    @SuppressLint("Range")
    public List<Task> getAllTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TasksContract.TaskEntry.TABLE_NAME, null, null, null, null, null, null);
        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry._ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_DESCRIPTION)));
                task.setDueDate(cursor.getLong(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_DUE_DATE)));
                task.setCategoryId(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_CATEGORY_ID)));
                task.setPriority(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_PRIORITY)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_COMPLETED)) == 1);
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public List<Long> getAllTaskDates() {
        List<Task> tasks = getAllTasks();
        Set<Long> dateSet = new TreeSet<>();
        Calendar cal = Calendar.getInstance();
        for (Task t : tasks) {
            cal.setTimeInMillis(t.getDueDate());
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            dateSet.add(cal.getTimeInMillis());
        }
        return new ArrayList<>(dateSet);
    }

    @SuppressLint("Range")
    public List<Task> getTasksByDate(long dayTimestamp) {
        long startOfDay = dayTimestamp;
        long endOfDay = startOfDay + 24 * 60 * 60 * 1000;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = TasksContract.TaskEntry.COLUMN_DUE_DATE + " >= ? AND " +
                TasksContract.TaskEntry.COLUMN_DUE_DATE + " < ?";
        String[] selectionArgs = { String.valueOf(startOfDay), String.valueOf(endOfDay) };

        Cursor cursor = db.query(
                TasksContract.TaskEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        List<Task> tasks = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry._ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_DESCRIPTION)));
                task.setDueDate(cursor.getLong(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_DUE_DATE)));
                task.setCategoryId(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_CATEGORY_ID)));
                task.setPriority(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_PRIORITY)));
                task.setCompleted(cursor.getInt(cursor.getColumnIndex(TasksContract.TaskEntry.COLUMN_COMPLETED)) == 1);
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return tasks;
    }

    public void reassignTasksToCategory(int oldCategoryId, int newCategoryId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TasksContract.TaskEntry.COLUMN_CATEGORY_ID, newCategoryId);
        db.update(
                TasksContract.TaskEntry.TABLE_NAME,
                values,
                TasksContract.TaskEntry.COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(oldCategoryId)}
        );
    }

    public int getIncompleteTaskCountByCategory(int categoryId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + TasksContract.TaskEntry.TABLE_NAME +
                " WHERE " + TasksContract.TaskEntry.COLUMN_CATEGORY_ID + " = ?" +
                " AND " + TasksContract.TaskEntry.COLUMN_COMPLETED + " = 0";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}