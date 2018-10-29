package com.ewind.hl.ui.history.chart;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dant.centersnapreyclerview.SnappingRecyclerView;
import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.history.HistoryItemDetailView;
import com.ewind.hl.ui.history.chart.period.HistoryPeriod;

import java.util.List;

public class HistoryChartAdapter<D extends EventDetail>
        extends RecyclerView.Adapter<HistoryChartAdapter.HistoryChartViewHolder> {

    public static final String TAG = HistoryChartAdapter.class.getSimpleName();

    private List<ChartItem> items;
    private final EventUI<D> ui;

    private SnappingRecyclerView recyclerView;

    private int position;

    public HistoryChartAdapter(EventUI<D> ui) {
        this.ui = ui;
    }

    public void setItems(List<ChartItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = (SnappingRecyclerView) recyclerView;

        scrollToPosition(this.position);
    }

    @NonNull
    @Override
    public HistoryChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_history_item, parent, false);

        FrameLayout detailLayout = view.findViewById(R.id.history_item_detail);
        HistoryItemDetailView detailView = ui.createHistoryItemDetail();
        detailLayout.addView(detailView);

        return new HistoryChartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryChartViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> scrollToPosition(position));
        holder.bindTo(items.get(position), position == this.position);
    }


    private int getPeriodPosition(HistoryPeriod period) {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).getPeriod().equals(period)) {
                return i;
            }
        }

        throw new IllegalArgumentException("No such period: " + period);
    }

    public void scrollToPeriod(HistoryPeriod period) {
        int newPosition = getPeriodPosition(period);
        scrollToPosition(newPosition);
    }

    private void scrollToPosition(int newPosition) {
        recyclerView.scrollToPosition(newPosition);
        onPositionChanged(newPosition);
    }

    public void onPositionChanged(int newPosition) {
        notifyItemChanged(newPosition);
        notifyItemChanged(position);
        position = newPosition;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public HistoryPeriod getPeriod(int position) {
        return items.get(position).getPeriod();
    }

    public static final class HistoryChartViewHolder extends RecyclerView.ViewHolder {

        private TextView labelView;
        private HistoryItemDetailView detailView;

        public HistoryChartViewHolder(View itemView) {
            super(itemView);

            labelView = itemView.findViewById(R.id.history_item_label);
            FrameLayout detailLayout = itemView.findViewById(R.id.history_item_detail);
            detailView = (HistoryItemDetailView) detailLayout.getChildAt(0);
        }

        public void bindTo(ChartItem item, boolean isSelected) {
            String text = isSelected ? "" : item.getPeriod().getShortLabel();
            labelView.setText(text);

            detailView.setItem(item, isSelected);
        }
    }
}
