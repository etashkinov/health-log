package com.ewind.hl.ui.history.chart.period;

import org.joda.time.LocalDate;


public class HistoryPeriodFactory {
    public enum HistoryPeriodType {
        DAY,
        MONTH,
        YEAR
    }

    public static HistoryPeriod toPeriod(LocalDate localDate, HistoryPeriodType type) {
        switch (type) {
            case DAY:
                return new DayHistoryPeriod(localDate);
            case MONTH:
                return new MonthHistoryPeriod(localDate.getYear(), localDate.getMonthOfYear());
            case YEAR:
                return new YearHistoryPeriod(localDate.getYear());
            default:
                throw new IllegalArgumentException("Unexpected period " + type);
        }
    }
}
