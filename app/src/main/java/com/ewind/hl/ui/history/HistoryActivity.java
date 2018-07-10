package com.ewind.hl.ui.history;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.LocalizationService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = HistoryActivity.class.getName();
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_AREA = "EVENT_AREA";

    private EventDate from;
    private EventDate till;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_chart);

        EventType type = (EventType) getIntent().getSerializableExtra(EVENT_TYPE);
        Area area = (Area) getIntent().getSerializableExtra(EVENT_AREA);

        from = new EventDate(LocalDate.now().minusDays(30), DayPart.ALL_DAY);
        till = new EventDate(LocalDate.now(), DayPart.PM_11);

        initHeader();

        List<Event> events = new EventsDao(this).getEvents(type, area, from, till);
        if (!events.isEmpty()) {
            initChart(events);
        }
    }

    private void initHeader() {
        Resources res = getResources();
        String text = String.format(res.getString(R.string.historyChartDates), from, till);
        TextView historyChartDates = findViewById(R.id.historyChartDates);
        historyChartDates.setText(text);
    }

    private void initChart(List<Event> events) {
        LineChart chart = findViewById(R.id.historyChart);

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

    private static final class EventEntry extends Entry {
        private final Event event;

        private EventEntry(Event event) {
            super(toScalar(event.getDate()), (float)event.getDetail().getScore());
            this.event = event;
        }

        private static float toScalar(EventDate date) {
            return (date.getLocalDate().getYear() - 2018) * 365
                    + date.getLocalDate().getMonthOfYear() * 30
                    + date.getLocalDate().getDayOfMonth();
        }
    }
}
