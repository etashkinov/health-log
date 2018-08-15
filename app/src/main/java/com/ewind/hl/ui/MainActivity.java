package com.ewind.hl.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventComparator;
import com.ewind.hl.model.event.EventDateComparator;
import com.ewind.hl.model.event.EventRelevancyComparator;
import com.ewind.hl.model.event.EventScoreComparator;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventsDao;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EventChangedListener {

    private static final String TAG = MainActivity.class.getName();
    private EventAdapter adapter;
    private boolean showAll = false;
    private TextView showAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AreaFactory.initBody(this); // FIXME
        EventTypeFactory.initEvents(this);

        setContentView(R.layout.main_activity);
        setSupportActionBar(findViewById(R.id.toolbar));

        findViewById(R.id.addNewButton).setOnClickListener(this::onEventAdd);
        showAllButton = findViewById(R.id.eventsListAllButton);
        showAllButton.setOnClickListener(this::onShowAll);

        RecyclerView eventsList = findViewById(R.id.eventsList);


        EventComparator comparator = new EventComparator(
                new EventRelevancyComparator(),
                new EventDateComparator(),
                new EventScoreComparator()
        );

        adapter = new EventAdapter(comparator) {
            @Override
            public EventItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                EventActionListener listener = new EventActionListener(MainActivity.this);
                return new LastEventItemViewHolder(parent, listener);
            }
        };
        eventsList.setAdapter(adapter);
        refreshEvents();
    }

    private void onShowAll(View view) {
        showAll = !showAll;
        refreshEvents();
    }

    private void refreshEvents() {
        showAllButton.setText(showAll ? "Show less" : "Show all");
        adapter.setEvents(getEvents());
    }

    private List<Event> getEvents() {
        List<Event> latestEvents = new EventsDao(this).getLatestEvents();
        List<Event> eventsToShow = new LinkedList<>();
        for (Event latestEvent : latestEvents) {
            if (showAll || latestEvent.getType().isRelevant(latestEvent)) {
                eventsToShow.add(latestEvent);
            }
        }
        return eventsToShow;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return item.getItemId() == R.id.action_settings
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String eventType = data.getStringExtra(EventTypeSearchActivity.SELECTED_EVENT_TYPE);
            if (eventType != null) {
                new EventActionListener(this).onAddNew(eventType);
            } else {
                refreshEvents();
            }
        }
    }

    public void onEventAdd(View view) {
        new EventActionListener(this).onSelectEventType();
    }

    @Override
    public void onEventCreated(Event event) {
        refreshEvents();
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
