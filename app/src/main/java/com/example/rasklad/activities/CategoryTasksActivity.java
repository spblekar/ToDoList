package com.example.rasklad.activities;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.adapters.TaskAdapter;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Task;
import java.util.ArrayList;
import java.util.List;

public class CategoryTasksActivity extends AppCompatActivity {
    private RecyclerView rvTasks;
    private TaskAdapter adapter;
    private TaskRepository taskRepository;
    private TextView textViewCategoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        rvTasks = findViewById(R.id.rvTasks);
        textViewCategoryName = findViewById(R.id.textViewDayDate);

        int categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryName = getIntent().getStringExtra("categoryName");
        textViewCategoryName.setText("Категория: " + categoryName);

        taskRepository = new TaskRepository(this);
        List<Task> tasks = new ArrayList<>();
        for (Task task : taskRepository.getAllTasks()) {
            if (task.getCategoryId() == categoryId) {
                tasks.add(task);
            }
        }

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, tasks);
        rvTasks.setAdapter(adapter);
    }
}