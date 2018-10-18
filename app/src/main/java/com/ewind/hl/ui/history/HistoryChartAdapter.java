package com.ewind.hl.ui.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dant.centersnapreyclerview.SnappingRecyclerView;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.history.period.HistoryPeriod;
import com.ewind.hl.ui.history.period.HistoryPeriodFactory;
import com.ewind.hl.ui.history.period.HistoryPeriodFactory.HistoryPeriodType;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HistoryChartAdapter<D extends EventDetail>
        extends RecyclerView.Adapter<HistoryChartAdapter.HistoryChartViewHolder> {

    public static final String TAG = HistoryChartAdapter.class.getSimpleName();

    private List<HistoryItem<D>> items;
    private final EventUI<D> ui;

    private SnappingRecyclerView recyclerView;

    private int position;

    public HistoryChartAdapter(EventUI<D> ui, List<Event<D>> events, HistoryPeriodType periodType) {
        this.ui = ui;
        updateItems(events, periodType);
    }

    private void updateItems(List<Event<D>> events, HistoryPeriodType periodType) {
        Map<HistoryPeriod, List<Event<D>>> grouping = groupByPeriod(events, periodType);
        HistoryPeriod from = Collections.min(grouping.keySet()).add(-10);
        HistoryPeriod now = HistoryPeriodFactory.toPeriod(LocalDate.now(), periodType);
        HistoryPeriod till = now.add(10);

        int steps = till.minus(from);
        this.items = new ArrayList<>(steps);
        for (int i = 0; i <= steps; i++) {
            HistoryPeriod period = from.add(i);
            this.items.add(new HistoryItem<>(period, grouping.get(period)));
            if (period.equals(now)) {
                this.position = i;
            }
        }
        Log.i(TAG, "History from " + from + " till " + till + ": " + steps);
    }

    public void setItems(List<Event<D>> events, HistoryPeriodType periodType) {
        updateItems(events, periodType);
        notifyDataSetChanged();
        scrollToPosition(position);
    }

    @NonNull
    private Map<HistoryPeriod, List<Event<D>>> groupByPeriod(List<Event<D>> events, HistoryPeriodType periodType) {
        Map<HistoryPeriod, List<Event<D>>> grouping = new HashMap<>();
        for (Event<D> event : events) {
            HistoryPeriod period = HistoryPeriodFactory.toPeriod(event.getDate().getLocalDate(), periodType);
            List<Event<D>> periodEvents = grouping.get(period);
            if (periodEvents == null) {
                periodEvents = new LinkedList<>();
                grouping.put(period, periodEvents);
            }

            periodEvents.add(event);
        }
        return grouping;
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
        HistoryItemDetailView detailView = ui.createHistoryItemDetail(context);
        detailLayout.addView(detailView);

        return new HistoryChartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryChartViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> scrollToPosition(position));
        holder.bindTo((List)items, position, position == this.position);
    }

    private void scrollToPosition(int position) {
        Log.i(HistoryChartAdapter.class.getName(), "Scroll to: " + position);
        recyclerView.scrollToPosition(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public HistoryPeriod getPeriod(int position) {
        return items.get(position).period;
    }

    public List<Event<D>> getEvents(int position) {
        return items.get(position).events;
    }

    public void setPosition(int newPosition) {
        int oldPosition = this.position;
        this.position = newPosition;

        notifyItemChanged(oldPosition);
        notifyItemChanged(newPosition);
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

        public void bindTo(List<HistoryItem<?>> items, int position, boolean isSelected) {
            HistoryItem<?> item = items.get(position);
            String text = isSelected ? "" : item.period.getShortLabel();
            labelView.setText(text);

            detailView.setItem(items, position, isSelected);
        }
    }

    public static final class HistoryItem<D extends EventDetail> {
        private final HistoryPeriod period;
        protected final List<Event<D>> events;

        private HistoryItem(HistoryPeriod period, List<Event<D>> events) {
            this.period = period;
            this.events = events == null ? Collections.emptyList() : new ArrayList<>(events);

            Collections.sort(this.events, new EventDateComparator());
        }
    }
}
