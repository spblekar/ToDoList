package com.example.rasklad.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.activities.TaskEditActivity;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.models.Task;
import com.example.rasklad.utils.DateUtils;
import com.example.rasklad.utils.PriorityUtils;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> tasks;
    private TaskRepository taskRepository;

    public TaskAdapter(Context context, List<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        this.taskRepository = new TaskRepository(context);
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(task.isCompleted());

        holder.checkBoxCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            taskRepository.updateTask(task);
        });

        holder.textViewTitle.setText(task.getTitle());
        holder.textViewDate.setText(DateUtils.formatDate(task.getDueDate()));
        holder.priorityIndicator.setBackgroundColor(PriorityUtils.getColorForPriority(task.getPriority()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TaskEditActivity.class);
            intent.putExtra("taskId", task.getId());
            context.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Удаление задачи")
                    .setMessage("Вы уверены, что хотите удалить задачу \"" + task.getTitle() + "\"?")
                    .setPositiveButton("Да", (dialog, which) -> {
                        taskRepository.deleteTask(task.getId());
                        tasks.remove(position);
                        notifyItemRemoved(position);
                    })
                    .setNegativeButton("Нет", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxCompleted;
        TextView textViewTitle;
        TextView textViewDate;
        View priorityIndicator;

        public TaskViewHolder(View itemView) {
            super(itemView);
            priorityIndicator = itemView.findViewById(R.id.viewPriority);
            checkBoxCompleted = itemView.findViewById(R.id.checkBoxTaskCompleted);
            textViewTitle = itemView.findViewById(R.id.textViewTaskTitle);
            textViewDate = itemView.findViewById(R.id.textViewTaskDate);
        }
    }
}