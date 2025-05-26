package com.example.rasklad.models;

public class PreviewDayData {
    private long date;
    private String dayName;
    private int totalTaskCount;
    private int completedTaskCount;
    private int incompleteTaskCount;
    private int lowPriorityCount;
    private int mediumPriorityCount;
    private int highPriorityCount;

    public PreviewDayData(long date, String dayName,
                          int totalTaskCount, int completedTaskCount, int incompleteTaskCount,
                          int lowPriorityCount, int mediumPriorityCount, int highPriorityCount) {
        this.date = date;
        this.dayName = dayName;
        this.totalTaskCount = totalTaskCount;
        this.completedTaskCount = completedTaskCount;
        this.incompleteTaskCount = incompleteTaskCount;
        this.lowPriorityCount = lowPriorityCount;
        this.mediumPriorityCount = mediumPriorityCount;
        this.highPriorityCount = highPriorityCount;
    }

    public long getDate() { return date; }
    public String getDayName() { return dayName; }
    public int getTotalTaskCount() { return totalTaskCount; }
    public int getCompletedTaskCount() { return completedTaskCount; }
    public int getIncompleteTaskCount() { return incompleteTaskCount; }
    public int getLowPriorityCount() { return lowPriorityCount; }
    public int getMediumPriorityCount() { return mediumPriorityCount; }
    public int getHighPriorityCount() { return highPriorityCount; }
}