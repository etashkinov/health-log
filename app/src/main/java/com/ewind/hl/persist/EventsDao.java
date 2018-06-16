package com.ewind.hl.persist;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EventsDao {

    private final static Map<EventDate, List<Event>> events = new HashMap<>();

    public static List<Event> doGetEvents(EventDate date) {
        List<Event> result = events.get(date);
        if (result == null) {
            result = new LinkedList<>();
            events.put(date, result);
        }
        return result;
    }

    public static List<Event> getEvents(Area area, EventDate date) {
        List<Event> dateEvents = doGetEvents(date);
        List<Event> result = new LinkedList<>();
        for (Event event : dateEvents) {
            if (event.getArea().equals(area)) {
                result.add(event);
            }
        }

        return Collections.unmodifiableList(result);
     }

    public static void store(Event<?> event) {
        List<Event> events = doGetEvents(event.getDate());
        events.add(event);
    }
}
