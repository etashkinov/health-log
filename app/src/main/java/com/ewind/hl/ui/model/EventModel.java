package com.ewind.hl.ui.model;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;

import java.io.Serializable;

public class EventModel implements Serializable {
    private final EventType type;
    private final EventDetail detail;
    private final String note;

    private final Area area;
    private final EventDate date;

    public static EventModel of(Event event) {
        return new EventModel(
                event.getType(),
                event.getDetail(),
                event.getNote(),
                event.getArea(),
                event.getDate());
    }

    public static EventModel copyOf(Event event, EventDate date) {
        return new EventModel(
                event.getType(),
                event.getDetail(),
                null,
                event.getArea(),
                date);
    }

    public static EventModel empty(EventType type, Area area, EventDate date) {
        return new EventModel(type, null, null, area, date);
    }

    public EventModel(EventType type, EventDetail detail, String note, Area area, EventDate date) {
        this.type = type;
        this.detail = detail;
        this.note = note;
        this.area = area;
        this.date = date;
    }

    public EventType getType() {
        return type;
    }

    public EventDetail getDetail() {
        return detail;
    }

    public String getNote() {
        return note;
    }

    public Area getArea() {
        return area;
    }

    public EventDate getDate() {
        return date;
    }

    public Event toEvent(long id) {
        return new Event(id, date, type, detail, area, note);
    }

}
