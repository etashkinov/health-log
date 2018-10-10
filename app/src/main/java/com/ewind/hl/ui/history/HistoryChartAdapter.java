package com.ewind.hl.ui.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventScoreComparator;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventUIFactory;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HistoryChartAdapter extends RecyclerView.Adapter<HistoryChartAdapter.HistoryChartViewHolder> {

    private final List<HistoryItem> items;

    public HistoryChartAdapter(List<Event> events) {
        LocalDate from = LocalDate.now().minusDays(30);
        LocalDate till = LocalDate.now().plusDays(5);
        Map<LocalDate, List<Event<?>>> grouping = new HashMap<>();
        for (Event<?> event : events) {
            LocalDate day = event.getDate().getLocalDate();
            if (day.isBefore(from)) {
                from = day;
            }

            List<Event<?>> dayEvents = grouping.get(day);
            if (dayEvents == null) {
                dayEvents = new LinkedList<>();
                grouping.put(day, dayEvents);
            }

            dayEvents.add(event);
        }

        items = new LinkedList<>();
        LocalDate current = from;
        while (!current.isAfter(till)) {
            items.add(new HistoryItem(current, grouping.get(current)));
            current = current.plusDays(1);
        }
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
        holder.bindTo(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final class HistoryChartViewHolder extends RecyclerView.ViewHolder {

        private TextView chartBarLabel;
        private ImageView chartBar;

        public HistoryChartViewHolder(View itemView) {
            super(itemView);

            chartBarLabel = itemView.findViewById(R.id.chart_bar_label);
            chartBar = itemView.findViewById(R.id.chart_bar);
        }

        public void bindTo(HistoryItem item) {
            chartBarLabel.setText(String.valueOf(item.date.getDayOfMonth()));

            if (item.events == null) {
                chartBar.setVisibility(View.INVISIBLE);
            } else {
                chartBar.setVisibility(View.VISIBLE);
                Event<?> maxEvent = Collections.min(item.events, new EventScoreComparator());

                float scale = chartBar.getContext().getResources().getDisplayMetrics().density;
                int heightInPx  = (int) ((maxEvent.getScore().getValue() * 2 + 20) * scale);

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
        private final List<Event<?>> events;

        private HistoryItem(LocalDate date, List<Event<?>> events) {
            this.date = date;
            this.events = events;
        }
    }
}
