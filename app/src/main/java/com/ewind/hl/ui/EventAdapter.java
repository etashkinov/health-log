package com.ewind.hl.ui;

import android.support.v7.widget.RecyclerView;

import com.ewind.hl.model.event.Event;

import java.util.List;

public abstract class EventAdapter extends RecyclerView.Adapter<EventItemViewHolder> {

    private final List<Event> events;

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    @Override
    public void onBindViewHolder(EventItemViewHolder holder, int position) {
        holder.setEvent(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}
