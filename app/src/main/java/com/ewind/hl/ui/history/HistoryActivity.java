package com.ewind.hl.ui.history;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.EventActionListener;
import com.ewind.hl.ui.EventChangedListener;
import com.ewind.hl.ui.LocalizationService;
import com.ewind.hl.ui.history.chart.HistoryChartFragment;
import com.ewind.hl.ui.view.area.AreaSelector;

import java.util.Collections;
import java.util.List;

public class HistoryActivity<D extends EventDetail> extends AppCompatActivity implements EventChangedListener {
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_AREA = "EVENT_AREA";

    protected EventType<D> type;
    protected Area area;

    private List<Event<D>> events;
    private MenuItem actionChart;
    private MenuItem actionList;
    private boolean chartFragment = true;
    private View addButton;

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

        addButton = findViewById(R.id.addButton);

        refreshEvents();
    }

    private void refreshEvents() {
        events = findEvents();
        if (!events.isEmpty()) {
            Event<?> lastEvent = events.get(0);
            addButton.setOnClickListener(v -> onAdd(lastEvent));
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

    protected List<Event<D>> findEvents() {
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
        getSupportActionBar().setTitle(title);

        AreaSelector areaSelector = findViewById(R.id.areaSelector);
        if (EventTypeFactory.getAreas(type).size() < 2) {
            areaSelector.setVisibility(View.GONE);
        } else {
            areaSelector.init(type, area);
        }
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
        Fragment fragment = chartFragment ? new HistoryChartFragment() : new HistoryListFragment();
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

    public List<Event<D>> getEvents() {
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

    public EventType<D> getType() {
        return type;
    }
}