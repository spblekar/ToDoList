package com.example.rasklad.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.activities.DayDetailsActivity;
import com.example.rasklad.utils.DateUtils;
import java.util.List;

public class PreviewDayAdapter extends RecyclerView.Adapter<PreviewDayAdapter.DayViewHolder> {
    private Context context;
    private List<Pair<Long, Integer>> daysWithCounts;

    public PreviewDayAdapter(Context context, List<Pair<Long, Integer>> daysWithCounts) {
        this.context = context;
        this.daysWithCounts = daysWithCounts;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_preview_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        Pair<Long, Integer> dayData = daysWithCounts.get(position);
        long date = dayData.first;
        int count = dayData.second;

        holder.textViewDayDate.setText(DateUtils.getRelativeDayName(date));
        holder.textViewDayCount.setText("Задач: " + count);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DayDetailsActivity.class);
            intent.putExtra("date", date);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return daysWithCounts != null ? daysWithCounts.size() : 0;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDayDate;
        TextView textViewDayCount;

        public DayViewHolder(View itemView) {
            super(itemView);
            textViewDayDate = itemView.findViewById(R.id.textViewDayDate);
            textViewDayCount = itemView.findViewById(R.id.textViewDayCount);
        }
    }
}