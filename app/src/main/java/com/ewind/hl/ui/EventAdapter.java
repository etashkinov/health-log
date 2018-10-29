package com.ewind.hl.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.EventItemViewHolder.EventItem;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventUIFactory;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class EventAdapter extends RecyclerView.Adapter<EventItemViewHolder> {

    private List<EventItem> eventItems = Collections.emptyList();
    private final Comparator<Event> comparator;

    protected EventAdapter(Comparator<Event> comparator) {
        this.comparator = comparator;
    }

    public void setEventItems(List<Event> events, Context context) {
        events = events == null ? Collections.emptyList() : new ArrayList<>(events);
        Collections.sort(events, comparator);
        this.eventItems = new ArrayList<>(events.size());
        for (Event event : events) {
            this.eventItems.add(createEventItem(context, event));
        }
    }

    @NonNull
    protected <D extends EventDetail> EventItem createEventItem(Context context, Event<D> event) {
        String dateLabel = LocalizationService.getEventDateFrom(event.getDate(), LocalDateTime.now());
        EventType<D> type = event.getType();
        EventUI<D> ui = EventUIFactory.getUI(type, context);
        Drawable icon = ui.getEventTypeDrawable();
        int tint = ui.getEventTint(event);
        View detailsView = ui.getLastEventDetailView(event);
        return new EventItem(dateLabel, icon, tint, detailsView, event);
    }

    @Override
    public void onBindViewHolder(@NonNull EventItemViewHolder holder, int position) {
        holder.setEvent(eventItems.get(position));
    }

    @Override
    public int getItemCount() {
        return eventItems.size();
    }
}
