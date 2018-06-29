package com.ewind.hl.ui;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ewind.hl.model.event.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventItemViewHolder> {

    private final List<Event> events;
    private final EventActionListener listener;

    public EventAdapter(List<Event> events, EventActionListener listener) {
        this.events = events;
        this.listener = listener;
    }

    @Override
    public EventItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventItemViewHolder(parent, listener);
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
