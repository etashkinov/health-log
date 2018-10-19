package com.ewind.hl.ui.history.chart.period;

import android.support.annotation.NonNull;

import com.ewind.hl.model.event.EventDate;

import org.joda.time.Days;
import org.joda.time.LocalDate;


public class DayHistoryPeriod implements HistoryPeriod {

    private static final String THIS_YEAR_PATTERN = "d MMMM";
    private static final String ANOTHER_YEAR_PATTERN = "d MMMM 'yy";

    private final LocalDate day;

    public DayHistoryPeriod(LocalDate day) {
        this.day = day;
    }

    public static DayHistoryPeriod of(EventDate eventDate) {
        return new DayHistoryPeriod(eventDate.getLocalDate());
    }

    @Override
    public String getLabel() {
        String pattern = day.getYear() == LocalDate.now().getYear() ? THIS_YEAR_PATTERN : ANOTHER_YEAR_PATTERN;
        return day.toString(pattern);
    }

    @Override
    public String getShortLabel() {
        return String.valueOf(day.getDayOfMonth());
    }

    @Override
    public boolean contains(EventDate eventDate) {
        return eventDate.getStart().equals(day);
    }

    @Override
    public DayHistoryPeriod add(int steps) {
        return new DayHistoryPeriod(day.plusDays(steps));
    }

    @Override
    public int minus(@NonNull HistoryPeriod o) {
        return Days.daysBetween(((DayHistoryPeriod)o).day, day).getDays();
    }

    @Override
    public String toString() {
        return day.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DayHistoryPeriod that = (DayHistoryPeriod) o;

        return day.equals(that.day);
    }

    @Override
    public int hashCode() {
        return day.hashCode();
    }
}
