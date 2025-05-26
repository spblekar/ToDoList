package com.example.rasklad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.adapters.PreviewDayAdapter;
import com.example.rasklad.adapters.TaskAdapter;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Task;
import com.example.rasklad.utils.DateUtils;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvTodayTasks, rvPreviewDays;
    private TaskAdapter todayTaskAdapter;
    private PreviewDayAdapter previewDayAdapter;
    private TaskRepository taskRepository;
    private TextView textViewToday;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerViews();
        setupButtons();
        setupCalendar();

        loadData();
    }

    private void initViews() {
        textViewToday = findViewById(R.id.textViewToday);
        rvTodayTasks = findViewById(R.id.rvTodayTasks);
        rvPreviewDays = findViewById(R.id.rvPreviewDays);
        taskRepository = new TaskRepository(this);
    }

    private void setupRecyclerViews() {
        rvTodayTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTodayTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        rvPreviewDays.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupButtons() {
        findViewById(R.id.buttonAddTask).setOnClickListener(v ->
                startActivity(new Intent(this, TaskEditActivity.class)));

        findViewById(R.id.buttonCategories).setOnClickListener(v ->
                startActivity(new Intent(this, CategoriesActivity.class)));

        findViewById(R.id.buttonSettings).setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        if (textViewToday != null) {
            textViewToday.setOnClickListener(v -> {
                if (this.calendarView != null) {
                    this.calendarView.setDate(System.currentTimeMillis(), true, true);
                }
            });
        }
    }

    private void setupCalendar() {
        this.calendarView = findViewById(R.id.calendarView);
        if (this.calendarView != null) {
            this.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth, 0, 0, 0);
                cal.set(Calendar.MILLISECOND, 0);

                startActivity(new Intent(this, DayDetailsActivity.class)
                        .putExtra("date", cal.getTimeInMillis()));
            });
        }
    }

    private void loadData() {
        String fullDate = DateUtils.formatFullDate(System.currentTimeMillis());
        fullDate = Character.toUpperCase(fullDate.charAt(0)) + fullDate.substring(1);
        textViewToday.setText(fullDate);

        loadTodayTasks();
        loadPreviewDays();
    }

    private void loadTodayTasks() {
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        List<Task> todayTasks = taskRepository.getTasksByDate(today.getTimeInMillis());

        if (todayTasks != null && !todayTasks.isEmpty()) {
            todayTasks.sort(Comparator.comparingLong(Task::getDueDate));
        }

        todayTaskAdapter = new TaskAdapter(this, todayTasks);
        rvTodayTasks.setAdapter(todayTaskAdapter);

        if (todayTasks.isEmpty()) {
            findViewById(R.id.emptyTodayView).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.emptyTodayView).setVisibility(View.GONE);
        }
    }

    private void loadPreviewDays() {
        Calendar todayCal = Calendar.getInstance();
        todayCal.set(Calendar.HOUR_OF_DAY, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);
        long todayStart = todayCal.getTimeInMillis();

        List<Long> dates = taskRepository.getAllTaskDates().stream()
                .filter(date -> date >= todayStart)
                .sorted()
                .limit(3)
                .collect(Collectors.toList());

        previewDayAdapter = new PreviewDayAdapter(this, dates);
        rvPreviewDays.setAdapter(previewDayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}