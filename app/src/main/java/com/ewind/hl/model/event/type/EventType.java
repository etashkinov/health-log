package com.ewind.hl.model.event.type;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Accuracy;
import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.ScoreBand;
import com.ewind.hl.model.event.type.EventTypeFactory.EventConfig;
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

    protected EventType(String name, Class<D> detailClass, EventConfig config) {
        this.name = name;
        this.accuracy = getAccuracy(config.getAccuracy());

        if (config.getExpiration() != null) {
            this.expiration = Period.parse(config.getExpiration());
        } else {
            this.expiration = Period.hours(accuracy.toHours() / 2);
        }

        this.areas = getAreas(config);
        this.detailClass = detailClass;
    }

    public Event<D> create(LocalDateTime from, Area area, D detail) {
        if (detail == null) {
            detail = createNormalDetail();
        }

        if (area == null && getAreas().size() == 1) {
            area = AreaFactory.getArea(getAreas().iterator().next());
        }

        return new Event<>(0L, getEventDate(from), this, detail, area, null, getScore(detail));
    }

    protected abstract D createNormalDetail();

    private EventDate getEventDate(LocalDateTime from) {
        return new EventDate(from.toLocalDate(), DayPart.partOf(from.getHourOfDay(), accuracy));
    }

    public boolean isExpired(Event event) {
        LocalDateTime expirationTime = event.getDate().getEnd().plus(expiration);
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

    @NonNull
    private static Accuracy getAccuracy(String accuracyStr) {
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

    public String getDescription(Event<D> event, Context context) {
        return getDescription(event.getDetail(), context);
    }

    public abstract Score getScore(D detail);

    public ScoreBand getScoreBand(D detail) {
        return new ScoreBand(getScore(detail));
    }

    public abstract String getDescription(D detail, Context context);

    public boolean isRelevant(Event event) {
        return event.getDate().getEnd().plusDays(2).isAfter(LocalDateTime.now());
    }

    public boolean updateNeeded(Event event) {
        return isExpired(event) && !event.getScore().isNormal();
    }
}
