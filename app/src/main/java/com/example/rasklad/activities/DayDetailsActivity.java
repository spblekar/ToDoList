package com.example.rasklad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.adapters.TaskAdapter;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Task;
import com.example.rasklad.utils.DateUtils;
import java.util.List;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DayDetailsActivity extends AppCompatActivity {
    private RecyclerView rvTasks;
    private TextView textViewDayDate, emptyDayView;
    private FloatingActionButton buttonAddTask;
    private TaskRepository taskRepository;
    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        textViewDayDate = findViewById(R.id.textViewDayDate);
        rvTasks         = findViewById(R.id.rvTasks);
        emptyDayView    = findViewById(R.id.emptyDayView);
        buttonAddTask   = findViewById(R.id.buttonAddTask);

        date = getIntent().getLongExtra("date", System.currentTimeMillis());

        String fullDate = DateUtils.formatFullDate(date);

        fullDate = Character.toUpperCase(fullDate.charAt(0)) + fullDate.substring(1);
        textViewDayDate.setText(fullDate);

        taskRepository = new TaskRepository(this);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        buttonAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(this, TaskEditActivity.class);
            intent.putExtra("date", date);
            startActivity(intent);
        });

        loadTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        List<Task> tasks = taskRepository.getTasksByDate(date);

        TaskAdapter adapter = new TaskAdapter(this, tasks);
        rvTasks.setAdapter(adapter);

        if (tasks.isEmpty()) {
            emptyDayView.setVisibility(View.VISIBLE);
            rvTasks.setVisibility(View.GONE);
        } else {
            emptyDayView.setVisibility(View.GONE);
            rvTasks.setVisibility(View.VISIBLE);
        }
    }
}