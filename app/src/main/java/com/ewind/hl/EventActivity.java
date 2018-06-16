package com.ewind.hl;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.view.EventButton;
import com.ewind.hl.view.EventDatePicker;
import com.ewind.hl.view.EventFormDialog;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventActivity extends AppCompatActivity {

    private static final String TAG = EventActivity.class.getName();
    private static final int MAX_EVENTS_NUMBER = 4;

    private Area area;
    private EventDate date;

    private ImageButton backButton;
    private Toolbar eventsBar;
    private RecyclerView partsList;
    private TextView currentAreaHeaderText;
    private EventDatePicker eventDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event);
        setSupportActionBar(findViewById(R.id.toolbar));

        eventsBar = findViewById(R.id.eventsBar);
        partsList = initPartsList();
        backButton = findViewById(R.id.areaBackButton);
        currentAreaHeaderText = findViewById(R.id.currentAreaHeaderText);
        eventDatePicker = findViewById(R.id.eventDatePicker);
        eventDatePicker.setListener(this::onDateChanged);

        this.area = loadBodyConfiguration();
        refreshArea();
        eventDatePicker.onDateChanged(EventDate.of(Calendar.getInstance()));
    }

    private void onAreaChanged(Area area) {
        if (!area.equals(this.area)) {
            this.area = area;
            refreshArea();
        }

        refreshEvents();
    }

    private void onDateChanged(EventDate date) {
        refreshEvents();
    }

    private RecyclerView initPartsList() {
        RecyclerView result = findViewById(R.id.partsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        result.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                result.getContext(),
                layoutManager.getOrientation());
        result.addItemDecoration(dividerItemDecoration);
        return result;
    }

    private Area loadBodyConfiguration() {
        try (InputStream stream = getResources().openRawResource(R.raw.body)) {
            return AreaFactory.getBody(stream);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load body configuration", e);
        }
    }

    private void refreshArea() {
        Area parent = area.getParent();
        if (parent != null) {
            Log.i(TAG, "Set back button visible for parent " + area);
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(v -> onAreaChanged(parent));
            currentAreaHeaderText.setText(area.getDescription());
        } else {
            Log.i(TAG, "Set back button invisible for null parent");
            backButton.setVisibility(View.INVISIBLE);
            backButton.setOnClickListener(null);
            currentAreaHeaderText.setText(R.string.eventTitleText);
        }

        partsList.setAdapter(new PartsAdapter(area.getParts(), this::onAreaChanged));
    }

    @NonNull
    private String getName(Area area) {
        String title = area.getName().replaceAll("_"," ");
        title = Character.toUpperCase(title.charAt(0)) + title.substring(1);
        return title;
    }

    private void refreshEvents() {
        List<EventType> eventTypes = area.getEvents();
        Log.i(TAG, "Set event buttons: " + eventTypes);

        List<Event> events = EventsDao.getEvents(area, eventDatePicker.getDate());
        Map<EventType, EventDetail> eventDetails = new HashMap<>();
        for (Event event : events) {
            EventDetail eventDetail = event.getValue();
            eventDetails.put(eventDetail.getType(), eventDetail);
        }

        eventsBar.removeAllViews();
        for (int i = 0; i < Math.min(MAX_EVENTS_NUMBER, eventTypes.size()); i++) {
            EventType type = eventTypes.get(i);
            addEventButton(eventsBar, type, eventDetails.get(type));
        }
    }

    private void addEventButton(ViewGroup parent, EventType type, EventDetail eventDetail) {
        EventButton eventButton = (EventButton) LayoutInflater.from(this)
                .inflate(R.layout.event_button, parent, false);

        eventButton.setEvent(type, eventDetail);
        eventButton.setOnEventClickListener(this::showEventForm);

        parent.addView(eventButton);
    }

    private void showEventForm(EventType type, EventDetail detail) {
        new EventFormDialog(this, type, detail, this::onEventChanged).show();
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

    public void onEventChanged(EventDetail detail) {
        Event<?> event = new Event<>(detail, eventDatePicker.getDate(), area, null);
        EventsDao.store(event);
        refreshEvents();
    }
}
