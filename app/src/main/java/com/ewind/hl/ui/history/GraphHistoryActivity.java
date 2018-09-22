package com.ewind.hl.ui.history;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.LocalizationService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GraphHistoryActivity extends AppCompatActivity {
    private static final String TAG = GraphHistoryActivity.class.getName();
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_AREA = "EVENT_AREA";

    private EventDate from;
    private EventDate till;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_chart);

        String type = getIntent().getStringExtra(EVENT_TYPE);
        Area area = (Area) getIntent().getSerializableExtra(EVENT_AREA);

        from = new EventDate(LocalDate.now().minusDays(300), DayPart.ALL_DAY);
        till = new EventDate(LocalDate.now(), DayPart.PM_11);

        initHeader();

        List<Event> events = new EventsDao(this).getEvents(type, area, from, till);
        if (!events.isEmpty()) {
            initChart(events);
        }
    }

    private void initHeader() {
        Resources res = getResources();
        String fromDate = LocalizationService.getLocalDate(from.getLocalDate());
        String tillDate = LocalizationService.getLocalDate(till.getLocalDate());
        String text = String.format(res.getString(R.string.historyChartDates), fromDate, tillDate);
        TextView historyChartDates = findViewById(R.id.historyChartDates);
        historyChartDates.setText(text);
    }

    private void initChart(List<Event> events) {
        LineChart chart = findViewById(R.id.historyChart);

        LineDataSet dataSet = getDataSet(events);
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
    private LineDataSet getDataSet(List<Event> events) {
        List<Entry> entries = new ArrayList<>();
        Collections.sort(events, new EventDateComparator(true));
        for (Event event : events) {
            Score score = event.getScore();
            entries.add(new EventEntry(event.getDate().getStart(), score));
            entries.add(new EventEntry(event.getDate().getEnd(), score));
        }

        EventType type = events.get(0).getType();
        LineDataSet dataSet = new LineDataSet(entries, LocalizationService.getEventTypeName(this, type));
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return dataSet;
    }

    private static final class EventEntry extends Entry {

        public EventEntry(LocalDateTime time, Score score) {
            super(toScalar(time), (float)score.getValue());
        }

        private static float toScalar(LocalDateTime date) {
            return date.getDayOfYear() * 24 + date.getHourOfDay();
        }
    }
}
