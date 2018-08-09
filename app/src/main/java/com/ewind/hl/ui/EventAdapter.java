package com.ewind.hl.ui;

import android.support.v7.widget.RecyclerView;

import com.ewind.hl.model.event.Event;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class EventAdapter extends RecyclerView.Adapter<EventItemViewHolder> {

    private List<Event> events;

    public void setEvents(List<Event> events) {
        this.events = new LinkedList<>(events);
        refreshEvents();
    }

    private void refreshEvents() {
        Collections.sort(events, (e1, e2) -> - e1.getDate().compareTo(e2.getDate()));
        notifyItemRangeChanged(0, events.size());
        notifyDataSetChanged();
    }

    public void addEvent(Event event) {
        this.events.add(event);
        refreshEvents();
    }

    public void removeEvent(Event event) {
        Iterator<Event> iterator = this.events.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId() == event.getId()) {
                iterator.remove();
            }
        }
        refreshEvents();
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
