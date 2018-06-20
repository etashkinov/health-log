package com.ewind.hl;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.ewind.hl.ui.view.EventButton;
import com.ewind.hl.ui.view.EventDatePicker;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private static final int MAX_EVENTS_NUMBER = 4;

    public class State {
        private final EventType type;
        private final Area area;
        private final EventDate date;

        private State(EventType type, Area area, EventDate date) {
            this.type = type;
            this.area = area;
            this.date = date;
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

        eventsBar = findViewById(R.id.eventsBar);
        backButton = findViewById(R.id.areaBackButton);
        currentAreaHeaderText = findViewById(R.id.currentAreaHeaderText);
        eventDatePicker = findViewById(R.id.eventDatePicker);
        eventDatePicker.setListener(this::onDateChanged);

        Area area = loadBodyConfiguration();
        EventDate date = EventDate.of(Calendar.getInstance());

        refresh(null, area, date);
    }

    public void onAreaChanged(Area area) {
        refresh(state.type, area, state.date);
    }

    private void onDateChanged(EventDate date) {
        refresh(state.type, state.area, date);
    }

    private void onEventTypeChanged(EventType type) {
        refresh(type == state.type ? null : type, state.area, state.date);
    }

    private void refresh(EventType type, Area area, EventDate date) {
        if (!area.getEvents().contains(type)) {
            type = null;
        }

        state = new State(type, area, date);
        refreshArea();
        refreshDate();
        refreshContent();
        new RefreshEntitiesTask(this).execute();
    }

    private void refreshDate() {
        eventDatePicker.setDate(state.getDate());
    }

    private void refreshContent() {
        if (state.getType() != null) {
            setContent(new EventFragment());
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

    private void refreshEvents(List<Event> events) {
        Area area = state.getArea();
        List<EventType> eventTypes = area.getEvents();
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
        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        fragTransaction.replace(R.id.mainContentContainer, fragment, fragment.getClass().getSimpleName());
        fragTransaction.commit();
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
        new UpdateEventTask(this, detail).execute();
    }

    public void onEventSearch(View view) {
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(R.layout.event_search).create();
        dialog.show();

        ListView listView = dialog.findViewById(R.id.eventsList);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, EventType.values()));
        listView.setOnItemClickListener((parent,v,position,id) -> {
            dialog.cancel();
            onEventTypeChanged(EventType.values()[position]);
        });

    }

    public State getState() {
        return state;
    }

    private static class RefreshEntitiesTask extends AsyncTask<Void, Void, List<Event>> {
        //Prevent leak
        private final WeakReference<MainActivity> weakActivity;

        private RefreshEntitiesTask(MainActivity weakActivity) {
            this.weakActivity = new WeakReference<>(weakActivity);
        }

        @Override
        protected List<Event> doInBackground(Void... voids) {
            MainActivity activity = this.weakActivity.get();
            return new EventsDao(activity).getEvents(activity.state.area, activity.state.date);
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            this.weakActivity.get().refreshEvents(events);
        }
    }

    private static class UpdateEventTask extends AsyncTask<Void, Void, Void> {
        //Prevent leak
        private final WeakReference<MainActivity> weakActivity;
        private final EventDetail detail;

        private UpdateEventTask(MainActivity weakActivity, EventDetail detail) {
            this.weakActivity = new WeakReference<>(weakActivity);
            this.detail = detail;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (detail != null) {
                MainActivity activity = weakActivity.get();
                new EventsDao(activity).store(new Event<>(detail, activity.state.getDate(), activity.state.getArea(), null));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            MainActivity activity = weakActivity.get();
            activity.refresh(null, activity.state.area, activity.state.date);
        }
    }
}
