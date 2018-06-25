package com.ewind.hl.ui.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class HistoryFragment extends Fragment {
    private static final String TAG = HistoryFragment.class.getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_chart, null);

        MainActivity.State state = ((MainActivity) getActivity()).getState();
        EventDate till = state.getDate();
        EventDate from = getFromDate(till);

        initHeader(view, till, from);
        initChart(view, state, till, from);

        view.findViewById(R.id.eventFormButton).setOnClickListener(this::onShowForm);

        return view;
    }

    private void onShowForm(View view) {
        ((MainActivity) getActivity()).onModeChanged(MainActivity.ViewMode.FORM);
    }

    private void initHeader(View view, EventDate till, EventDate from) {
        Resources res = getResources();
        String text = String.format(res.getString(R.string.historyChartDates), from, till);
        TextView historyChartDates = view.findViewById(R.id.historyChartDates);
        historyChartDates.setText(text);
    }

    private void initChart(View view, MainActivity.State state, EventDate till, EventDate from) {
        LineChart chart = view.findViewById(R.id.historyChart);

        List<Entry> entries = new ArrayList<>();

        List<Event> events = new EventsDao(getContext()).getEvents(state.getType(), state.getArea(),
                from,
                till);
        for (Event event : events) {
            entries.add(new EventEntry(event));
        }

        EventType type = events.get(0).getValue().getType();
        LineDataSet dataSet = new LineDataSet(entries, LocalizationService.getEventTypeName(type)); // add entries to dataset
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
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
            super(toScalar(event.getDate()), (float)event.getValue().getScore());
            this.event = event;
        }

        private static float toScalar(EventDate date) {
            return (date.getYear() - 2018) * 365 + date.getMonth() * 30 + date.getDay();
        }
    }
}
