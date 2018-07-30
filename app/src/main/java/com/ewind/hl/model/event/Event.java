package com.ewind.hl.model.event;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.detail.EventDetail;

import java.io.Serializable;

public class Event<T extends EventDetail> implements Serializable {

    private final long id;
    private final EventDate date;
    private final EventType<T> type;
    private final T detail;
    private final Area area;
    private final String note;
    private final Score score;

    public Event(long id, EventDate date, EventType<T> type, T detail, Area area, String note, Score score) {
        this.id = id;
        this.date = date;
        this.type = type;
        this.detail = detail;
        this.area = area;
        this.note = note;
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public EventDate getDate() {
        return date;
    }

    public EventType<T> getType() {
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
        return getType().isExpired(this);
    }

    public Score getScore() {
        return score;
    }
}
