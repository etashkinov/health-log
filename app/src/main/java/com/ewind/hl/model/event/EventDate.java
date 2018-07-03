package com.ewind.hl.model.event;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.Locale;

public class EventDate implements Serializable {
    private final LocalDate localDate;

    private final DayPart dayPart;
    private final Integer hour;

    public EventDate(LocalDate localDate, DayPart dayPart, Integer hour) {
        this.localDate = localDate;
        this.dayPart = dayPart;
        this.hour = hour;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public DayPart getDayPart() {
        return dayPart;
    }

    public Integer getHour() {
        return hour;
    }

    @Override
    public String toString() {
        return localDate.toString("yyyy-MM-dd")
                + (dayPart == null ? "" : " " + dayPart)
                + (hour == null ? "" : String.format(Locale.getDefault(), " %02d", hour));
    }

    public LocalDateTime getStart() {
        int hour = this.hour != null
                    ? this.hour
                    : (dayPart != null ? dayPart.getFirstHour() : 0);
        return localDate.toLocalDateTime(new LocalTime(hour, 0));
    }

    public LocalDateTime getEnd() {
        int hour = this.hour != null
                ? this.hour + 1
                : (dayPart != null ? dayPart.getLastHour() : 0);
        return localDate.toLocalDateTime(new LocalTime(hour, 0)).minusMillis(1);
    }
}
