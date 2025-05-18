package com.example.rasklad.models;

public class Task {
    private int id;
    private String title;
    private String description;
    private long dueDate;
    private int categoryId;
    private int priority;
    private boolean completed;

    public Task() {
    }

    public Task(int id, String title, String description, long dueDate, int categoryId, int priority, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.categoryId = categoryId;
        this.priority = priority;
        this.completed = completed;
    }

    public Task(String title, String description, long dueDate, int categoryId, int priority, boolean completed) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.categoryId = categoryId;
        this.priority = priority;
        this.completed = completed;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public long getDueDate() { return dueDate; }
    public void setDueDate(long dueDate) { this.dueDate = dueDate; }
    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}