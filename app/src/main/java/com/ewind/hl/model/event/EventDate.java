package com.ewind.hl.model.event;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.io.Serializable;

public class EventDate implements Serializable, Comparable<EventDate> {

    private final LocalDate localDate;

    private final DayPart dayPart;

    public EventDate(LocalDate localDate, DayPart dayPart) {
        this.localDate = localDate;
        this.dayPart = dayPart;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public DayPart getDayPart() {
        return dayPart;
    }

    @Override
    public String toString() {
        return localDate.toString("yyyy-MM-dd") + ' ' + dayPart;
    }

    public LocalDateTime getStart() {
        return localDate.toLocalDateTime(dayPart.getStart());
    }

    public LocalDateTime getEnd() {
        return localDate.toLocalDateTime(dayPart.getEnd());
    }

    @Override
    public int compareTo(@NonNull EventDate o) {
        int result = localDate.compareTo(o.localDate);
        return result != 0 ? result : dayPart.compareTo(o.dayPart);
    }
}
