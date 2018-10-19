package com.ewind.hl.ui.history.chart.period;

import android.support.annotation.NonNull;

import com.ewind.hl.model.event.EventDate;

import org.joda.time.LocalDate;
import org.joda.time.Months;


public class MonthHistoryPeriod extends YearHistoryPeriod {

    private static final String THIS_YEAR_PATTERN = "MMMM";
    private static final String ANOTHER_YEAR_PATTERN = "MMMM ''YY";

    private final int month;

    public static MonthHistoryPeriod of(EventDate eventDate) {
        LocalDate day = eventDate.getLocalDate();
        return new MonthHistoryPeriod(day.getYear(), day.getMonthOfYear());
    }

    public MonthHistoryPeriod(int year, int month) {
        super(year);
        this.month = month;
    }

    @Override
    public String getLabel() {
        String pattern = year == LocalDate.now().getYear() ? THIS_YEAR_PATTERN : ANOTHER_YEAR_PATTERN;
        return getFirstDay().toString(pattern);
    }

    @NonNull
    public LocalDate getFirstDay() {
        return super.getFirstDay().withMonthOfYear(month);
    }

    @Override
    public String getShortLabel() {
        return getFirstDay().toString("MMM");
    }

    @Override
    public boolean contains(EventDate eventDate) {
        return super.contains(eventDate)
                && eventDate.getLocalDate().getMonthOfYear() == month;
    }

    @Override
    public MonthHistoryPeriod add(int steps) {
        LocalDate newDay = getFirstDay().plusMonths(steps);
        return new MonthHistoryPeriod(newDay.getYear(), newDay.getMonthOfYear());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MonthHistoryPeriod that = (MonthHistoryPeriod) o;

        return month == that.month && year == that.year;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + month;
        return result;
    }

    @Override
    public int minus(@NonNull HistoryPeriod o) {
        MonthHistoryPeriod other = (MonthHistoryPeriod) o;
        return Months.monthsBetween(
                new LocalDate(other.year, other.month, 1),
                new LocalDate(year, month, 1)).getMonths();
    }
}
