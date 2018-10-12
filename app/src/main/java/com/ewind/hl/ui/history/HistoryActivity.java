package com.ewind.hl.ui.history;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventChangedListener;
import com.ewind.hl.ui.LocalizationService;

import java.util.Collections;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements EventChangedListener {
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_AREA = "EVENT_AREA";

    protected EventType<?> type;
    protected Area area;

    private List<Event> events;
    private MenuItem actionChart;
    private MenuItem actionList;
    private boolean chartFragment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        type = EventTypeFactory.get(getIntent().getStringExtra(EVENT_TYPE));
        area = (Area) getIntent().getSerializableExtra(EVENT_AREA);

        initHeader(type, area);

        refreshEvents();
    }

    private void refreshEvents() {
        events = findEvents();
        if (!events.isEmpty()) {
            Event<?> lastEvent = events.get(0);
            findViewById(R.id.addButton).setOnClickListener(v -> onAdd(lastEvent));
        } else {
            new EventActionListener(this).onAddNew(type.getName());
        }

        refreshFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data);
        }

        refreshEvents();
    }

    private void onAdd(Event<?> lastEvent) {
        new EventActionListener(this).onAddLike(lastEvent);
    }

    protected List<Event> findEvents() {
        return new EventsDao(this).getEvents(type, area);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    protected void initHeader(EventType<?> type, Area area) {
        String title = LocalizationService.getEventTypeName(this, type);
        if (type.getAreas().size() > 1) {
            title = LocalizationService.getAreaName(area) + " " + title;
        }

        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_history_menu, menu);
        actionChart = menu.findItem(R.id.action_history_chart);
        actionList = menu.findItem(R.id.action_history_list);

        refreshMenuActions();
        return true;
    }

    private void refreshFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = chartFragment ? new ChartHistoryFragment() : new ListHistoryFragment();
        ft.replace(R.id.history_fragment, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history_list:
                chartFragment = true;
                break;
            case R.id.action_history_chart:
                chartFragment = false;
                break;
            default:
                return false;
        }

        refreshMenuActions();
        refreshFragment();
        return true;
    }

    private void refreshMenuActions() {
        actionChart.setVisible(chartFragment);
        actionList.setVisible(!chartFragment);
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public void onEventCreated(Event event) {
        // Do nothing
    }

    @Override
    public void onEventUpdated(Event event) {
        refreshEvents();
    }

    @Override
    public void onEventDeleted(Event event) {
        refreshEvents();
    }
}