package com.ewind.hl.model.event;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.EventDetail;

import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class EventType<D extends EventDetail> implements Serializable {

    private final String name;
    private final Period expiration;
    private final Accuracy accuracy;
    private final Set<String> areas;
    private final Class<D> detailClass;
    private final boolean propagateDown;

    protected EventType(String name, Class<D> detailClass, EventConfig config) {
        this.name = name;
        this.accuracy = getAccuracy(config);
        this.expiration = Period.hours(accuracy.toHours());
        this.areas = getAreas(config);
        this.detailClass = detailClass;
        this.propagateDown = config.isPropagateDown();
    }

    public Event<D> create(LocalDateTime from, Area area, D detail) {
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
    public Class<D> getDetailClass() {
        return detailClass;
    }

    public boolean isPropagateDown() {
        return propagateDown;
    }

    @NonNull
    private static Accuracy getAccuracy(EventConfig eventConfig) {
        String accuracyStr = eventConfig.getAccuracy();
        Accuracy accuracy;
        if (accuracyStr == null) {
            accuracy = Accuracy.QUARTER;
        } else {
            accuracy = Accuracy.valueOf(accuracyStr.toUpperCase());
        }
        return accuracy;
    }

    private static Set<String> getAreas(EventConfig eventConfig) {
        Set<String> areas;
        Set<String> configAreas = eventConfig.getAreas();
        if (eventConfig.isPropagateDown()) {
            areas = new HashSet<>();
            for (String configArea : configAreas) {
                addArea(areas, AreaFactory.getArea(configArea));
            }
        } else {
            areas = configAreas;
        }
        return areas;
    }

    private static void addArea(Set<String> areas, Area area) {
        areas.add(area.getName());
        for (Area part : area.getParts()) {
            addArea(areas, part);
        }
    }

    public abstract String getDescription(Event<D> event, Context context);


    public abstract boolean isAbnormal(Event<D> event);
}
