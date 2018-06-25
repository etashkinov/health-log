package com.ewind.hl.model.event;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class EventDate implements Serializable {
    private final int year;
    private final int month;
    private final int day;

    @NonNull
    public static EventDate of(Calendar calendar) {
        return new EventDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
    }

    public static EventDate of(String date) {
        String[] split = date.split("-");
        return new EventDate(Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
        );
    }

    public EventDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    @NonNull
    public Calendar toCalendar() {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.YEAR, year);
        result.set(Calendar.MONTH, month);
        result.set(Calendar.DAY_OF_MONTH, day);
        return result;
    }

    @NonNull
    public EventDate yesterday() {
        return plusDays(-1);
    }

    @NonNull
    public EventDate tomorrow() {
        return plusDays(1);
    }

    @NonNull
    private EventDate plusDays(int amount) {
        Calendar calendar = toCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        return EventDate.of(calendar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDate eventDate = (EventDate) o;
        return year == eventDate.year &&
                month == eventDate.month &&
                day == eventDate.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public String toString() {
        return year + "-" + (month + 1) + "-" + day;
    }
}
