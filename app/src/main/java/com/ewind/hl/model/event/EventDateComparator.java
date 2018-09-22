package com.ewind.hl.model.event;

import java.util.Comparator;

public class EventDateComparator implements Comparator<Event> {
    private final int order;

    public EventDateComparator() {
        this(false);
    }

    public EventDateComparator(boolean asc) {
        order = asc ? 1 : -1;
    }

    public int compare(Event e1, Event e2) {
        return order * e1.getDate().compareTo(e2.getDate());
    }
}
