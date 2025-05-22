package com.example.rasklad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.activities.CategoryTasksActivity;
import com.example.rasklad.database.repository.CategoryRepository;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Category;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categories;
    private CategoryRepository categoryRepository;
    private TaskRepository taskRepository;


    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.categoryRepository = new CategoryRepository(context);
        this.taskRepository = new TaskRepository(context);
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.textViewCategoryName.setText(category.getName());

        int defaultId = categoryRepository.getOrCreateDefaultCategoryId();

        if (category.getId() == defaultId) {
            holder.buttonEdit.setEnabled(false);
            holder.buttonDelete.setEnabled(false);
            holder.buttonEdit.setVisibility(View.GONE);
            holder.buttonDelete.setVisibility(View.GONE);
        } else {
            holder.buttonEdit.setEnabled(true);
            holder.buttonDelete.setEnabled(true);
            holder.buttonEdit.setAlpha(1f);
            holder.buttonDelete.setAlpha(1f);

            holder.buttonDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(context)
                        .setTitle("Удаление категории")
                        .setMessage("Удалить категорию \"" + category.getName() + "\" и перенести задачи в \"Без категории\"?")
                        .setPositiveButton("Удалить", (dialog, which) -> {
                            int defaultCategoryId = categoryRepository.getOrCreateDefaultCategoryId();
                            taskRepository.reassignTasksToCategory(category.getId(), defaultCategoryId);
                            categoryRepository.deleteCategory(category.getId());
                            categories.remove(position);
                            notifyItemRemoved(position);
                        })
                        .setNegativeButton("Отмена", null)
                        .show();
            });

        }

        holder.textViewCategoryName.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryTasksActivity.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("categoryName", category.getName());
            context.startActivity(intent);
        });

        holder.buttonEdit.setOnClickListener(v -> {
            final EditText editText = new EditText(context);
            editText.setText(category.getName());

            new AlertDialog.Builder(context)
                    .setTitle("Редактировать категорию")
                    .setView(editText)
                    .setPositiveButton("Сохранить", (dialog, which) -> {
                        String newName = editText.getText().toString().trim();
                        if (!newName.isEmpty()) {
                            category.setName(newName);
                            categoryRepository.updateCategory(category);
                            notifyItemChanged(position);
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        int taskCount = taskRepository.getIncompleteTaskCountByCategory(category.getId());
        holder.textViewTaskCount.setText("Задач: " + taskCount);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategoryName;
        TextView textViewTaskCount;
        TextView buttonEdit;
        TextView buttonDelete;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
            textViewTaskCount = itemView.findViewById(R.id.textViewTaskCount);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}