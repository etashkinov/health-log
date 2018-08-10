package com.ewind.hl.model.event;

public class EventDateComparator extends EventComparator {

    public EventDateComparator() {
        super(null);
    }

    protected int doCompare(Event e1, Event e2) {
        return - e1.getDate().compareTo(e2.getDate());
    }
}
