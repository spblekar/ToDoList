package com.example.rasklad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rasklad.R;
import com.example.rasklad.adapters.PreviewDayAdapter;
import com.example.rasklad.database.repository.TaskRepository;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView rvPreviewDays;
    private PreviewDayAdapter adapter;
    private TaskRepository taskRepository;
    private Button buttonCategories;
    private Button buttonSettings;
    private Button buttonAddTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvPreviewDays = findViewById(R.id.rvPreviewDays);
        buttonCategories = findViewById(R.id.buttonCategories);
        buttonSettings = findViewById(R.id.buttonSettings);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        taskRepository = new TaskRepository(this);
        rvPreviewDays.setLayoutManager(new LinearLayoutManager(this));
        loadPreviewDays();

        buttonAddTask.setOnClickListener(v -> {
            startActivity(new Intent(this, TaskEditActivity.class));
        });
        buttonCategories.setOnClickListener(v -> {
            startActivity(new Intent(this, CategoriesActivity.class));
        });
        buttonSettings.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadPreviewDays();
    }

    private void loadPreviewDays() {
        List<Long> dates = taskRepository.getAllTaskDates();
        adapter = new PreviewDayAdapter(this, dates);
        rvPreviewDays.setAdapter(adapter);
    }
}
