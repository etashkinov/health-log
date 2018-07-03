package com.ewind.hl.model.event;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.detail.EventDetail;

import org.joda.time.LocalDateTime;

public class Event<T extends EventDetail> {

    private final long id;
    private final EventDate date;
    private final EventType type;
    private final T detail;
    private final Area area;
    private final String note;

    public Event(long id, EventDate date, EventType type, T detail, Area area, String note) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.detail = detail;
        this.area = area;
        this.note = note;
    }

    public long getId() {
        return id;
    }

    public EventDate getDate() {
        return date;
    }

    public EventType getType() {
        return type;
    }

    public T getDetail() {
        return detail;
    }

    public Area getArea() {
        return area;
    }

    public String getNote() {
        return note;
    }

    public boolean isExpired() {
        LocalDateTime expirationTime = getDate().getStart().plus(getType().getExpiration());
        return expirationTime.isBefore(LocalDateTime.now());
    }
}
