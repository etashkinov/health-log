package com.ewind.hl.model.event;

import android.support.annotation.NonNull;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.detail.EventDetail;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.Set;

public abstract class EventType<T extends EventDetail> implements Serializable {

    private final String name;
    private final Period expiration;
    private final Accuracy accuracy;
    private final Set<String> areas;
    private final Class<T> detailClass;
    private final boolean propagateDown;

    protected EventType(String name, Period expiration, Accuracy accuracy, Set<String> areas, Class<T> detailClass, boolean propagateDown) {
        this.name = name;
        this.expiration = expiration;
        this.accuracy = accuracy;
        this.areas = areas;
        this.detailClass = detailClass;
        this.propagateDown = propagateDown;
    }

    public Event<T> create(LocalDateTime from, Area area, T detail) {
        return new Event<>(0L, getEventDate(from), this, detail, area, null);
    }

    private EventDate getEventDate(LocalDateTime from) {
        return new EventDate(from.toLocalDate(), DayPart.partOf(from.getHourOfDay(), accuracy));
    }

    public boolean isExpired(Event event) {
        LocalDateTime expirationTime = event.getDate().getStart().plus(expiration);
        return expirationTime.isBefore(LocalDateTime.now());
    }

    public String getName() {
        return name;
    }

    public Period getExpiration() {
        return expiration;
    }

    public Accuracy getAccuracy() {
        return accuracy;
    }

    public Set<String> getAreas() {
        return areas;
    }

    @NonNull
    public Class<T> getDetailClass() {
        return detailClass;
    }

    public boolean isPropagateDown() {
        return propagateDown;
    }
}
