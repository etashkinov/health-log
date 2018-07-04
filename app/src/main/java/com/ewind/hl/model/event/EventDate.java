package com.ewind.hl.model.event;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.io.Serializable;

import static org.joda.time.Period.days;
import static org.joda.time.Period.hours;

public class EventDate implements Serializable {

    public static final Period DAY = days(1);
    public static final Period QUARTER = hours(6);
    public static final Period HOUR = hours(1);

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
}
