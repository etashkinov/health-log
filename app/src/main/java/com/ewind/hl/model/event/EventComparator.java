package com.ewind.hl.model.event;

import java.util.Comparator;

public abstract class EventComparator implements Comparator<Event> {

    private final EventComparator downstream;

    protected EventComparator(EventComparator downstream) {
        this.downstream = downstream;
    }

    @Override
    public int compare(Event e1, Event e2) {
        int result = doCompare(e1, e2);
        return result == 0 && downstream != null ? downstream.compare(e1, e2) : result;
    }

    protected abstract int doCompare(Event e1, Event e2);
}
