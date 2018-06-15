package com.ewind.hl.controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewind.hl.EventActivity;
import com.ewind.hl.EventFormFragment;
import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.view.EventButton;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaViewController {

    private static final String TAG = "AreaViewController";
    private static final int MAX_EVENTS_NUMBER = 4;

    private final ImageButton backButton;
    private final Toolbar eventsBar;
    private final RecyclerView partsList;
    private final TextView currentAreaHeaderText;
    private final EventActivity activity;

    private List<Event> events;
    private Area area;

    public AreaViewController(EventActivity activity) {
        this.activity = activity;

        RelativeLayout areaView = activity.findViewById(R.id.areaView);
        this.eventsBar = areaView.findViewById(R.id.eventsBar);
        this.partsList = initPartsList(areaView);
        this.backButton = areaView.findViewById(R.id.areaBackButton);
        this.currentAreaHeaderText = areaView.findViewById(R.id.currentAreaHeaderText);

        this.events = Collections.emptyList();

        initArea(areaView);
    }

    private static RecyclerView initPartsList(RelativeLayout areaView) {
        RecyclerView result = areaView.findViewById(R.id.partsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(areaView.getContext());
        result.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                result.getContext(),
                layoutManager.getOrientation());
        result.addItemDecoration(dividerItemDecoration);
        return result;
    }

    private void initArea(RelativeLayout areaView) {
        try (InputStream stream = areaView.getContext().getResources().openRawResource(R.raw.body)) {
            setArea(AreaFactory.getBody(stream));
        } catch (Exception e) {
            Log.e(TAG, "Failed to load body configuration", e);
        }
    }

    private void setArea(Area area) {
        Log.i(TAG, "Set area: " + area);

        this.area = area;

        this.currentAreaHeaderText.setText(area.getName());

        setParts(area);
        setBackButton(area);

        refreshEvents();
    }

    private void setParts(Area area) {
        List<Area> parts = area.getParts();
        Log.i(TAG, "Set parts: " + parts);
        partsList.setAdapter(new PartsAdapter(parts, this::setArea));
    }

    private void setBackButton(Area area) {
        Area parent = area.getParent();
        if (parent != null) {
            Log.i(TAG, "Set back button visible for parent " + area);
            backButton.setVisibility(View.VISIBLE);
            backButton.setOnClickListener(v -> setArea(parent));
        } else {
            Log.i(TAG, "Set back button invisible for null parent");
            backButton.setVisibility(View.INVISIBLE);
            backButton.setOnClickListener(null);
        }
    }

    private void refreshEvents() {
        List<EventType> eventTypes = area.getEvents();

        Log.i(TAG, "Set event buttons: " + eventTypes);

        Map<EventType, EventDetail> eventDetails = getAreaEventDetails();

        eventsBar.removeAllViews();
        for (int i = 0; i < Math.min(MAX_EVENTS_NUMBER, eventTypes.size()); i++) {
            EventType type = eventTypes.get(i);
            addEventButton(eventsBar, type, eventDetails.get(type));
        }
    }

    @NonNull
    private Map<EventType, EventDetail> getAreaEventDetails() {
        Map<EventType, EventDetail> eventDetails = new HashMap<>();
        for (Event event : this.events) {
            if (event.getArea().equals(area)) {
                EventDetail detail = event.getValue();
                eventDetails.put(detail.getType(), detail);
            }
        }
        return eventDetails;
    }

    private void addEventButton(ViewGroup parent, EventType type, EventDetail eventDetail) {
        EventButton eventButton = (EventButton) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_button, parent, false);

        eventButton.setEvent(type, eventDetail);
        eventButton.setOnEventClickListener(this::showEventForm);

        parent.addView(eventButton);
    }

    private void showEventForm(View view, EventType type, EventDetail detail) {
        EventFormFragment newFragment = new EventFormFragment();
        newFragment.setEvent(type, detail, d -> activity.onEventSubmit(area, d));
        newFragment.show(activity.getSupportFragmentManager(), "eventForm");
    }

    public void setEvents(List<Event> events) {
        this.events = events;
        refreshEvents();
    }

    public List<Event> getEvents() {
        return events;
    }
}
