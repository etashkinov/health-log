package com.ewind.hl.model.event;

public class EventScoreComparator extends EventComparator {

    public EventScoreComparator() {
        super(new EventDateComparator());
    }

    protected int doCompare(Event e1, Event e2) {
        return - e1.getScore().compareTo(e2.getScore());
    }
}
