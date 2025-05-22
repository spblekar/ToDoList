package com.example.rasklad.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.adapters.CategoryAdapter;
import com.example.rasklad.database.repository.CategoryRepository;
import com.example.rasklad.models.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {
    private RecyclerView rvCategories;
    private CategoryAdapter adapter;
    private CategoryRepository categoryRepository;
    private FloatingActionButton buttonAddCategory;

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
            editText.setHint("Введите название категории");
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.requestFocus();

            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Новая категория")
                    .setView(editText)
                    .setPositiveButton("Добавить", null)
                    .setNegativeButton("Отмена", (d, which) -> d.dismiss())
                    .create();

            dialog.setOnShowListener(dialogInterface -> {
                Button addButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                addButton.setOnClickListener(view -> {
                    String name = editText.getText().toString().trim();

                    if (name.isEmpty()) {
                        editText.setError("Введите название категории!");
                        return;
                    }

                    if (categoryRepository.isCategoryNameExists(name)) {
                        editText.setError("Категория уже существует!");
                        return;
                    }

                    long result = categoryRepository.addCategory(new Category(name));
                    if (result != -1) {
                        loadCategories();
                        dialog.dismiss();
                    }
                });
            });

            dialog.show();

            editText.post(() -> {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        });
    }

    private void loadCategories() {
        List<Category> categories = categoryRepository.getAllCategories();
        adapter = new CategoryAdapter(this, categories);
        rvCategories.setAdapter(adapter);
    }
}