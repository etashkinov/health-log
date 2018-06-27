package com.ewind.hl;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.ewind.hl.ui.LocalizationService;
import com.ewind.hl.ui.fragment.BodyFragment;
import com.ewind.hl.ui.fragment.EventFragment;
import com.ewind.hl.ui.fragment.HistoryFragment;
import com.ewind.hl.ui.view.EventButton;
import com.ewind.hl.ui.view.EventDatePicker;
import com.ewind.hl.ui.view.EventSearchView;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int MAX_EVENTS_NUMBER = 5;
    private static final String CONTENT_FRAGMENT_TAG = "content_fragment";

    public enum ViewMode {
        FORM,
        HISTORY,
        LIST
    }

    public class State {
        private final EventType type;
        private final Area area;
        private final EventDate date;
        private final ViewMode mode;

        private State(EventType type, Area area, EventDate date, ViewMode mode) {
            this.type = type;
            this.area = area;
            this.date = date;
            this.mode = mode;
        }

        public EventType getType() {
            return type;
        }

        public Area getArea() {
            return area;
        }

        public EventDate getDate() {
            return date;
        }
    }

    private State state;

    private ImageButton backButton;
    private ViewGroup eventsBar;
    private TextView currentAreaHeaderText;
    private EventDatePicker eventDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        setSupportActionBar(findViewById(R.id.toolbar));

//        eventsBar = findViewById(R.id.eventsBar);
//        backButton = findViewById(R.id.areaBackButton);
//        currentAreaHeaderText = findViewById(R.id.currentAreaHeaderText);
//        eventDatePicker = findViewById(R.id.eventDatePicker);
//        eventDatePicker.setListener(this::onDateChanged);

        Area area = loadBodyConfiguration();
        EventDate date = EventDate.of(Calendar.getInstance());

        refresh(null, area, date, ViewMode.FORM);
    }

    public void onAreaChanged(Area area) {
        refresh(state.type, area, state.date, state.mode);
    }

    private void onDateChanged(EventDate date) {
        refresh(state.type, state.area, date, state.mode);
    }

    private void onEventTypeChanged(EventType type) {
        refresh(type == state.type ? null : type, state.area, state.date, state.mode);
    }

    public void onModeChanged(ViewMode mode) {
        refresh(state.type, state.area, state.date, mode);
    }

    private void refresh(EventType type, Area area, EventDate date, ViewMode mode) {
        if (!area.getEvents().contains(type)) {
            type = null;
        }

        state = new State(type, area, date, mode);

        refresh();
    }

    private void refresh() {
        refreshArea();
        refreshDate();
        refreshContent();
        refreshEvents();
    }

    private void refreshDate() {
        eventDatePicker.setDate(state.getDate());
    }

    private void refreshContent() {
        if (state.getType() != null) {
            switch (state.mode) {
                case LIST:
                case FORM:
                    setContent(new EventFragment());
                    break;
                case HISTORY:
                    setContent(new HistoryFragment());
                    break;
                default:
                    throw new IllegalStateException("Unknown view mode: " + state.mode);
            }
        } else {
            setContent(new BodyFragment());
        }
    }


    private Area loadBodyConfiguration() {
        try (InputStream stream = getResources().openRawResource(R.raw.body)) {
            return AreaFactory.getBody(stream);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load body configuration", e);
        }
    }

    private void refreshArea() {
        Area area = state.getArea();
        Area parent = area.getParent();
        if (parent != null) {
            Log.i(TAG, "Set back button visible for parent " + area);
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(v -> onAreaChanged(parent));
            currentAreaHeaderText.setText(LocalizationService.getAreaName(area));
        } else {
            Log.i(TAG, "Set back button invisible for null parent");
            backButton.setVisibility(View.INVISIBLE);
            backButton.setOnClickListener(null);
            currentAreaHeaderText.setText(R.string.eventTitleText);
        }
    }

    private void refreshEvents() {
        List<Event> events = new EventsDao(this).getEvents(state.area, state.date);
        List<EventType> eventTypes = state.area.getEvents();
        Log.i(TAG, "Set event buttons: " + eventTypes);

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
        eventButton.setOnEventClickListener(this::onEventTypeChanged);

        parent.addView(eventButton);
    }

    private void setContent(Fragment fragment) {
//        FragmentManager fragMan = getFragmentManager();
//        FragmentTransaction fragTransaction = fragMan.beginTransaction();
//
//        fragTransaction.replace(R.id.mainContentContainer, fragment, CONTENT_FRAGMENT_TAG);
//        fragTransaction.commit();
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

    public void onEventUpdated(EventDetail detail) {
        if (detail != null) {
            new EventsDao(this).store(new Event<>(detail, state.date, state.area, null));
        }

        onEventTypeChanged(null);
    }

    public void onEventSearch(View view) {
        final EventSearchView eventSearchView = (EventSearchView) LayoutInflater.from(this).inflate(R.layout.event_search, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(eventSearchView).create();
        eventSearchView.setOnEventClickListener(e -> {
            dialog.cancel();
            onEventTypeChanged(e);
        });

        dialog.show();
    }

    public void onEventAdd(View view) {
        final EventSearchView eventSearchView = (EventSearchView) LayoutInflater.from(this).inflate(R.layout.event_search, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(eventSearchView).create();
        eventSearchView.setOnEventClickListener(e -> {
            dialog.cancel();
            onEventTypeChanged(e);
        });

        dialog.show();
    }



    public State getState() {
        return state;
    }
}
