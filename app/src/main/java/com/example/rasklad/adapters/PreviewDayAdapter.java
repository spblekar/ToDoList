package com.example.rasklad.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.activities.DayDetailsActivity;
import com.example.rasklad.database.repository.TaskRepository;
import com.example.rasklad.utils.DateUtils;
import java.util.List;

public class PreviewDayAdapter extends RecyclerView.Adapter<PreviewDayAdapter.DayViewHolder> {
    private Context context;
    private List<Long> dates;
    private TaskRepository taskRepository;

    public PreviewDayAdapter(Context context, List<Long> dates) {
        this.context = context;
        this.dates = dates;
        this.taskRepository = new TaskRepository(context);
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_preview_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        long date = dates.get(position);

        holder.textViewDayDate.setText(DateUtils.getRelativeDayName(date));

        int count = taskRepository.getTasksByDate(date).size();
        holder.textViewDayCount.setText("Задач: " + count);

        if (DateUtils.isToday(date)) {
            holder.itemView.setBackgroundColor(Color.parseColor("#80E0E0E0"));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DayDetailsActivity.class);
            intent.putExtra("date", date);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDayDate;
        TextView textViewDayCount;

        public DayViewHolder(View itemView) {
            super(itemView);
            textViewDayDate = itemView.findViewById(R.id.textViewDayDate);
            textViewDayCount = itemView.findViewById(R.id.textViewDayCount);
        }
    }
}