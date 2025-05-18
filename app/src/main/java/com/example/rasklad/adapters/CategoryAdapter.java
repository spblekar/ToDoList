package com.example.rasklad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rasklad.R;
import com.example.rasklad.database.repository.CategoryRepository;
import com.example.rasklad.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context context;
    private List<Category> categories;
    private CategoryRepository categoryRepository;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.categoryRepository = new CategoryRepository(context);
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

        holder.itemView.setOnClickListener(v -> {
            final EditText editText = new EditText(context);
            editText.setText(category.getName());
            new AlertDialog.Builder(context)
                    .setTitle("Редактировать категорию")
                    .setView(editText)
                    .setPositiveButton("Сохранить", (dialog, which) -> {
                        category.setName(editText.getText().toString());
                        categoryRepository.updateCategory(category);
                        notifyItemChanged(position);
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Удаление категории")
                    .setMessage("Вы уверены, что хотите удалить категорию \"" + category.getName() + "\"?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        categoryRepository.deleteCategory(category.getId());
                        categories.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Нет", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewCategoryName;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            textViewCategoryName = itemView.findViewById(R.id.textViewCategoryName);
        }
    }
}
