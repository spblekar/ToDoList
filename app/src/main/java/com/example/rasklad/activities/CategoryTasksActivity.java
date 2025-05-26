package com.example.rasklad.activities;

import android.os.Bundle;
import android.widget.ImageButton;
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
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_details);

        buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        rvTasks = findViewById(R.id.rvTasks);
        textViewCategoryName = findViewById(R.id.textViewDayDate);

        int categoryId = getIntent().getIntExtra("categoryId", -1);
        String categoryName = getIntent().getStringExtra("categoryName");

        if (textViewCategoryName != null) {
            textViewCategoryName.setText("Категория: " + categoryName);
        }

        taskRepository = new TaskRepository(this);
        List<Task> tasks;

        if (categoryId != -1) {
            tasks = taskRepository.getTasksByCategoryId(categoryId);
        } else {
            tasks = new ArrayList<>();
            if (textViewCategoryName != null) {
                textViewCategoryName.setText("Категория не найдена");
            }
        }

        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(this, tasks);
        rvTasks.setAdapter(adapter);
    }
}