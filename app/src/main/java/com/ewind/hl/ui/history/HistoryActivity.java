package com.ewind.hl.ui.history;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewind.hl.MainActivity;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.LocalizationService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.getName();
    public static final String EVENT_TYPE = "EVENT_TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_chart);

        EventType type = (EventType) getIntent().getSerializableExtra(EVENT_TYPE);
        EventDate till = new EventDate()

        MainActivity.State state = ((MainActivity) getActivity()).getState();
        EventDate till = state.getDate();
        EventDate from = getFromDate(till);

        initHeader(view, till, from);

        List<Event> events = new EventsDao(getContext()).getEvents(state.getType(), state.getArea(), from, till);
        if (!events.isEmpty()) {
            initChart(view, events);
        }
        view.findViewById(R.id.eventFormButton).setOnClickListener(this::onShowForm);
    }

    private void initHeader(View view, EventDate till, EventDate from) {
        Resources res = getResources();
        String text = String.format(res.getString(R.string.historyChartDates), from, till);
        TextView historyChartDates = view.findViewById(R.id.historyChartDates);
        historyChartDates.setText(text);
    }

    private void initChart(View view, List<Event> events) {
        LineChart chart = view.findViewById(R.id.historyChart);

        List<Entry> entries = new ArrayList<>();
        for (Event event : events) {
            entries.add(new EventEntry(event));
        }

        EventType type = events.get(0).getType();
        LineDataSet dataSet = new LineDataSet(entries, LocalizationService.getEventTypeName(type));
        dataSet.setColor(R.color.colorAccent);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.setDrawBorders(false);
        chart.setDrawGridBackground(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setEnabled(false);
        chart.invalidate(); // refresh
    }

    @NonNull
    private EventDate getFromDate(EventDate till) {
        Calendar monthAgo = till.toCalendar();
        monthAgo.add(Calendar.MONTH, -1);
        return EventDate.of(monthAgo);
    }

    private static final class EventEntry extends Entry {
        private final Event event;

        private EventEntry(Event event) {
            super(toScalar(event.getDate()), (float)event.getDetail().getScore());
            this.event = event;
        }

        private static float toScalar(EventDate date) {
            return (date.getYear() - 2018) * 365 + date.getMonth() * 30 + date.getDay();
        }
    }
}
