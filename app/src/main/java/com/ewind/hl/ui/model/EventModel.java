package com.ewind.hl.ui.model;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;

import java.io.Serializable;

public class EventModel<T extends EventDetail> implements Serializable {
    private final EventType<T> type;
    private final T detail;
    private final String note;

    private final Area area;
    private final EventDate date;

    public static <T extends EventDetail> EventModel<T> of(Event<T> event) {
        return new EventModel<>(
                event.getType(),
                event.getDetail(),
                event.getNote(),
                event.getArea(),
                event.getDate());
    }

    public static <T extends EventDetail>EventModel<T> copyOf(Event<T> event, EventDate date) {
        return new EventModel<>(
                event.getType(),
                event.getDetail(),
                null,
                event.getArea(),
                date);
    }

    public static <T extends EventDetail> EventModel<T> empty(EventType<T> type, Area area, EventDate date) {
        return new EventModel<>(type, null, null, area, date);
    }

    public EventModel(EventType<T> type, T detail, String note, Area area, EventDate date) {
        this.type = type;
        this.detail = detail;
        this.note = note;
        this.area = area;
        this.date = date;
    }

    public EventType<T> getType() {
        return type;
    }

    public T getDetail() {
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

    public Event<T> toEvent(long id) {
        return new Event<>(id, date, type, detail, area, note);
    }

}
