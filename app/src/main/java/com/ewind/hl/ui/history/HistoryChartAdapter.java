package com.ewind.hl.ui.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.dant.centersnapreyclerview.SnappingRecyclerView;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.EventScoreComparator;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventUIFactory;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HistoryChartAdapter extends RecyclerView.Adapter<HistoryChartAdapter.HistoryChartViewHolder> {

    private final List<HistoryItem> items;
    private int position;
    private SnappingRecyclerView recyclerView;

    public HistoryChartAdapter(List<Event> events) {
        LocalDate today = LocalDate.now();
        LocalDate from = today;
        Map<LocalDate, List<Event>> grouping = new HashMap<>();
        for (Event event : events) {
            LocalDate day = event.getDate().getLocalDate();
            if (day.isBefore(from)) {
                from = day;
            }

            List<Event> dayEvents = grouping.get(day);
            if (dayEvents == null) {
                dayEvents = new LinkedList<>();
                grouping.put(day, dayEvents);
            }

            dayEvents.add(event);
        }

        items = new LinkedList<>();
        int i = 0;
        LocalDate current = from.minusDays(10);
        while (!current.isAfter(today.plusDays(10))) {
            items.add(new HistoryItem(current, grouping.get(current)));
            current = current.plusDays(1);

            i++;
            if (current.equals(today)) {
                position = i;
            }
        }
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
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_chart_bar, parent, false);
        return new HistoryChartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryChartViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> scrollToPosition(position));
        holder.bindTo(items.get(position), position);
    }

    private void scrollToPosition(int position) {
        Log.i(HistoryChartAdapter.class.getName(), "Scroll to: " + position);
        recyclerView.scrollToPosition(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public LocalDate getDate(int position) {
        return items.get(position).date;
    }

    public List<Event> getEvents(int position) {
        return items.get(position).events;
    }

    public void setPosition(int newPosition) {
        int oldPosition = this.position;
        this.position = newPosition;

        notifyItemChanged(oldPosition);
        notifyItemChanged(newPosition);
    }

    public final class HistoryChartViewHolder extends RecyclerView.ViewHolder {

        private TextView chartBarLabel;
        private ImageView chartBar;

        public HistoryChartViewHolder(View itemView) {
            super(itemView);

            chartBarLabel = itemView.findViewById(R.id.chart_bar_label);
            chartBar = itemView.findViewById(R.id.chart_bar);
        }

        public void bindTo(HistoryItem item, int position) {
            String text = position == HistoryChartAdapter.this.position
                    ? ""
                    : String.valueOf(item.date.getDayOfMonth());
            chartBarLabel.setText(text);

            if (item.events.isEmpty()) {
                chartBar.setVisibility(View.INVISIBLE);
            } else {
                chartBar.setVisibility(View.VISIBLE);
                Event<?> maxEvent = Collections.min(item.events, new EventScoreComparator());

                float scale = chartBar.getContext().getResources().getDisplayMetrics().density;
                int heightInPx  = (int) ((maxEvent.getScore().getValue() + 20) * scale);

                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, heightInPx);
                params.gravity = Gravity.CENTER;
                chartBar.setLayoutParams(params);

                EventUI<?> ui = EventUIFactory.getUI(maxEvent.getType());
                ui.setEventTint(chartBar, maxEvent);
            }
        }
    }

    private final class HistoryItem {
        private final LocalDate date;
        private final List<Event> events;

        private HistoryItem(LocalDate date, List<Event> events) {
            this.date = date;
            this.events = events == null ? Collections.emptyList() : new ArrayList<>(events);

            Collections.sort(this.events, new EventDateComparator());
        }
    }
}
