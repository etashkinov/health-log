package com.ewind.hl.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaViewController {

    private static final String TAG = "AreaViewController";

    private final FloatingActionButton backButton;
    private final Toolbar eventsBar;

    private List<Event> events;
    private Area area;
    private final RecyclerView partsList;

    public AreaViewController(RelativeLayout areaView) {
        Context context = areaView.getContext();

        this.eventsBar = areaView.findViewById(R.id.eventsBar);

        this.partsList = areaView.findViewById(R.id.partsList);
        this.partsList.setLayoutManager(new LinearLayoutManager(context));

        this.backButton = areaView.findViewById(R.id.areaBackButton);

        this.events = Collections.emptyList();

        initArea(context);
    }

    private void initArea(Context context) {
        try (InputStream stream = context.getResources().openRawResource(R.raw.body)) {
            setArea(AreaFactory.getBody(stream));
        } catch (Exception e) {
            Log.e(TAG, "Failed to load body configuration", e);
        }
    }

    private void setArea(Area area) {
        Log.i(TAG, "Set area: " + area);

        this.area = area;

        setParts(area);
        setEventButtons(area);
        setBackButton(area);
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

    private void setEventButtons(Area area) {
        List<EventType> eventTypes = area.getEvents();

        Log.i(TAG, "Set event buttons: " + eventTypes);

        Map<EventType, EventDetail> eventDetails = getAreaEventDetails(area);

        eventsBar.removeAllViews();
        for (EventType type : eventTypes) {
            addEventButton(eventsBar, type, eventDetails.get(type));
        }
    }

    @NonNull
    private Map<EventType, EventDetail> getAreaEventDetails(Area area) {
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
        Button button = (Button) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.area_item_view, parent, false);

        String text = type.name();

        if (eventDetail != null) {
            text += '+';

            button.setOnClickListener(v ->
                    Snackbar.make(v, eventDetail.toString(), Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                            .show());
        }

        button.setText(text);

        parent.addView(button);
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }
}
