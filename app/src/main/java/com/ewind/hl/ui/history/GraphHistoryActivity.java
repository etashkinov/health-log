package com.ewind.hl.ui.history;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.persist.EventsDao;

import java.util.List;

public class GraphHistoryActivity extends AppCompatActivity {
    private static final String TAG = GraphHistoryActivity.class.getName();
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_AREA = "EVENT_AREA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        String type = getIntent().getStringExtra(EVENT_TYPE);
        Area area = (Area) getIntent().getSerializableExtra(EVENT_AREA);

        initHeader();

        List<Event> events = new EventsDao(this).getEvents(type, area);
        if (!events.isEmpty()) {
            initChart(events);
        }
    }

    private void initHeader() {
//        Resources res = getResources();
//        String fromDate = LocalizationService.getLocalDate(from.getLocalDate());
//        String tillDate = LocalizationService.getLocalDate(till.getLocalDate());
//        String text = String.format(res.getString(R.string.historyChartDates), fromDate, tillDate);
//        TextView historyChartDates = findViewById(R.id.historyChartDates);
//        historyChartDates.setText(text);
    }

    private void initChart(List<Event> events) {
        RecyclerView recyclerView = findViewById(R.id.history_chart);

        recyclerView.setAdapter(new HistoryChartAdapter(events));
    }
}
