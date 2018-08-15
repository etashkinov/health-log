package com.ewind.hl.model.event;

import java.util.Comparator;

public class EventDateComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        return - e1.getDate().compareTo(e2.getDate());
    }
}
