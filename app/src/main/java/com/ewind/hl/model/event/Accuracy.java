package com.ewind.hl.model.event;

import org.joda.time.LocalTime;
import org.joda.time.Period;

public enum Accuracy {
    DAY(Period.days(1)),
    QUARTER(Period.hours(6)),
    HOUR(Period.hours(1));

    private final Period period;

    Accuracy(Period period) {
        this.period = period;
    }

    public int toHours() {
        return period.toStandardHours().getHours();
    }

    public LocalTime endOf(LocalTime start) {
        return start.plus(period).minusMillis(1);
    }

    public static Accuracy valueOf(int period) {
        for (Accuracy accuracy : values()) {
            if (accuracy.toHours() == period) {
                return accuracy;
            }
        }

        throw new IllegalArgumentException("Unknown accuracy period: " + period);
    }
}
