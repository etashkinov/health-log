package com.ewind.hl.model.event;

import java.util.Comparator;

public class EventScoreComparator implements Comparator<Event> {
    public int compare(Event e1, Event e2) {
        return - e1.getScore().compareTo(e2.getScore());
    }
}
