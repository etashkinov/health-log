package com.ewind.hl.model.event;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {

    private final Comparator<Event>[] comparators;

    public EventComparator(Comparator<Event>... comparators) {
        this.comparators = comparators;
    }

    @Override
    public int compare(Event e1, Event e2) {
        for (Comparator<Event> comparator : comparators) {
            int result = comparator.compare(e1, e2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}
