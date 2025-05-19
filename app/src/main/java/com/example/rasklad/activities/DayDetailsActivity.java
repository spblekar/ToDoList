package com.example.rasklad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.adapters.TaskAdapter;
import com.example.rasklad.constants.AppConstants;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Task;
import com.example.rasklad.utils.DateUtils;
import com.example.rasklad.utils.PreferenceHelper;
import java.util.Collections;
import java.util.List;

public class DayDetailsActivity extends AppCompatActivity {
    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private TaskRepository taskRepository;
    private TextView textViewDayDate;
    private Button buttonAddTask;
    private long date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);
        rvTasks = findViewById(R.id.rvTasks);
        textViewDayDate = findViewById(R.id.textViewDayDate);
        buttonAddTask = findViewById(R.id.buttonAddTask);

        date = getIntent().getLongExtra("date", System.currentTimeMillis());
        textViewDayDate.setText(DateUtils.formatDate(date));

        taskRepository = new TaskRepository(this);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        loadTasks();

        buttonAddTask.setOnClickListener(v -> {
            startActivity(new Intent(this, TaskEditActivity.class).putExtra("date", date));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasks();
    }

    private void loadTasks() {
        List<Task> tasks = taskRepository.getTasksByDate(date);
        int sortOrder = PreferenceHelper.getSortOrder(this);
        if (sortOrder == AppConstants.SORT_BY_PRIORITY) {
            Collections.sort(tasks, (t1, t2) -> t2.getPriority() - t1.getPriority());
        }
        adapter = new TaskAdapter(this, tasks);
        rvTasks.setAdapter(adapter);
    }
}