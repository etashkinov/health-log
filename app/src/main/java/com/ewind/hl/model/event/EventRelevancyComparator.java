package com.ewind.hl.model.event;

import java.util.Comparator;

public class EventRelevancyComparator implements Comparator<Event> {

    public int compare(Event e1, Event e2) {
        int relevant1 = e1.getType().isRelevant(e1) ? 0 : 1;
        int relevant2 = e2.getType().isRelevant(e2) ? 0 : 1;
        return relevant1 - relevant2;
    }
}
