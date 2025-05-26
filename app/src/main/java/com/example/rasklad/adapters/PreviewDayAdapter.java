package com.example.rasklad.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rasklad.R;
import com.example.rasklad.activities.DayDetailsActivity;
import com.example.rasklad.models.PreviewDayData;
import java.util.List;
import java.util.Locale;

public class PreviewDayAdapter extends RecyclerView.Adapter<PreviewDayAdapter.DayViewHolder> {
    private Context context;
    private List<PreviewDayData> previewDayDataList;

    public PreviewDayAdapter(Context context, List<PreviewDayData> previewDayDataList) {
        this.context = context;
        this.previewDayDataList = previewDayDataList;
    }

    @Override
    public DayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_preview_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayViewHolder holder, int position) {
        PreviewDayData dayData = previewDayDataList.get(position);

        holder.textViewDayDate.setText(dayData.getDayName());

        String priorityText = String.format(Locale.getDefault(), "🟩%d 🟨%d 🟥%d",
                dayData.getLowPriorityCount(),
                dayData.getMediumPriorityCount(),
                dayData.getHighPriorityCount());
        holder.textViewDayPriority.setText(priorityText);

        String totalText = String.format(Locale.getDefault(), "Всего: %d", dayData.getTotalTaskCount());
        String completedText = String.format(Locale.getDefault(), "Вып.: %d", dayData.getCompletedTaskCount());
        String incompleteText = String.format(Locale.getDefault(), "Не вып.: %d", dayData.getIncompleteTaskCount());

        String statsInfo = totalText + "\n" +
                completedText + "\n" +
                incompleteText;

        holder.textViewDayCount.setText(statsInfo);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DayDetailsActivity.class);
            intent.putExtra("date", dayData.getDate());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return previewDayDataList != null ? previewDayDataList.size() : 0;
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDayDate;
        TextView textViewDayPriority;
        TextView textViewDayCount;

        public DayViewHolder(View itemView) {
            super(itemView);
            textViewDayDate = itemView.findViewById(R.id.textViewDayDate);
            textViewDayPriority = itemView.findViewById(R.id.textViewDayPriority);
            textViewDayCount = itemView.findViewById(R.id.textViewDayCount);
        }
    }
}