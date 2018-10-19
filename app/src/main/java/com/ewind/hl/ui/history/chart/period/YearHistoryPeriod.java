package com.ewind.hl.ui.history.chart.period;

import android.support.annotation.NonNull;

import com.ewind.hl.model.event.EventDate;

import org.joda.time.LocalDate;


public class YearHistoryPeriod implements HistoryPeriod {

    protected final int year;

    public static YearHistoryPeriod of(EventDate eventDate) {
        LocalDate day = eventDate.getLocalDate();
        return new YearHistoryPeriod(day.getYear());
    }


    public YearHistoryPeriod(int year) {
        this.year = year;
    }

    @Override
    public String getLabel() {
        return getFirstDay().toString("YYYY");
    }

    @NonNull
    public LocalDate getFirstDay() {
        return new LocalDate(year, 1, 1);
    }

    @Override
    public String getShortLabel() {
        return getFirstDay().toString("''YY");
    }

    @Override
    public boolean contains(EventDate eventDate) {
        return eventDate.getLocalDate().getYear() == year;
    }

    @Override
    public YearHistoryPeriod add(int steps) {
        return new YearHistoryPeriod(year + steps);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        YearHistoryPeriod that = (YearHistoryPeriod) o;

        return year == that.year;
    }

    @Override
    public int hashCode() {
        return year;
    }

    @Override
    public int minus(@NonNull HistoryPeriod o) {
        return year - ((YearHistoryPeriod)o).year;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
