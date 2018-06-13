package com.ewind.hl.model.event;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.detail.EventDetail;

public abstract class Event<T extends EventDetail> {
    private final T value;
    private final EventDate date;
    private final Area area;
    private final String note;

    public Event(T value, EventDate date, Area area, String note) {
        this.value = value;
        this.date = date;
        this.area = area;
        this.note = note;
    }

    public Area getArea() {
        return area;
    }

    public EventDate getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    public T getValue() {
        return value;
    }
}