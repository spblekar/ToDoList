package com.example.rasklad.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rasklad.R;
import com.example.rasklad.adapters.CategoryAdapter;
import com.example.rasklad.database.repository.CategoryRepository;
import com.example.rasklad.models.Category;

import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private CategoryRepository categoryRepository;
    private Button buttonAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        rvCategories = findViewById(R.id.rvCategories);
        buttonAddCategory = findViewById(R.id.buttonAddCategory);
        categoryRepository = new CategoryRepository(this);
        rvCategories.setLayoutManager(new LinearLayoutManager(this));
        loadCategories();

        buttonAddCategory.setOnClickListener(v -> {
            final EditText editText = new EditText(this);
            new AlertDialog.Builder(this)
                    .setTitle("Новая категория")
                    .setView(editText)
                    .setPositiveButton("Добавить", (dialog, which) -> {
                        String name = editText.getText().toString();
                        if (!name.isEmpty()) {
                            categoryRepository.addCategory(new Category(name));
                            loadCategories();
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });
    }

    private void loadCategories() {
        List<Category> categories = categoryRepository.getAllCategories();
        adapter = new CategoryAdapter(this, categories);
        rvCategories.setAdapter(adapter);
    }
}
