package com.ewind.hl.model.event;

import org.joda.time.LocalTime;

import static com.ewind.hl.model.event.Accuracy.DAY;
import static com.ewind.hl.model.event.Accuracy.HOUR;
import static com.ewind.hl.model.event.Accuracy.QUARTER;

public enum DayPart {

    ALL_DAY(0, DAY),

    NIGHT(0, QUARTER),
    MIDNIGHT(0, HOUR),
    AM_1(1, HOUR),
    AM_2(2, HOUR),
    AM_3(3, HOUR),
    AM_4(4, HOUR),
    AM_5(5, HOUR),
    MORNING(6, QUARTER),
    AM_6(6, HOUR),
    AM_7(7, HOUR),
    AM_8(8, HOUR),
    AM_9(9, HOUR),
    AM_10(10, HOUR),
    AM_11(11, HOUR),
    AFTERNOON(12, QUARTER),
    NOON(12, HOUR),
    PM_1(13, HOUR),
    PM_2(14, HOUR),
    PM_3(15, HOUR),
    PM_4(16, HOUR),
    PM_5(17, HOUR),
    EVENING(18, QUARTER),
    PM_6(18, HOUR),
    PM_7(19, HOUR),
    PM_8(20, HOUR),
    PM_9(21, HOUR),
    PM_10(22, HOUR),
    PM_11(23, HOUR);

    private final LocalTime start;
    private final Accuracy accuracy;

    DayPart(int hour, Accuracy accuracy) {
        this.start = new LocalTime(hour, 0);
        this.accuracy = accuracy;
    }

    public LocalTime getStart() {
        return start;
    }

    public int getStartHour() {
        return getStart().getHourOfDay();
    }

    public LocalTime getEnd() {
        return accuracy.endOf(start);
    }

    public Accuracy getAccuracy() {
        return accuracy;
    }

    public static DayPart valueOf(int firstHour, int period) {
        for (DayPart part : values()) {
            if (part.getStartHour() == firstHour
                    && Accuracy.valueOf(period) == part.accuracy) {
                return part;
            }
        }
        throw new IllegalArgumentException("Unknown hour: " + firstHour + " and period:  " + period);
    }

    public static DayPart partOf(int hour, Accuracy accuracy) {
        switch (accuracy) {
            case DAY:
                return ALL_DAY;
            case QUARTER:
                return quarterOf(hour);
            case HOUR:
                return hourOf(hour);
            default:
                throw new IllegalArgumentException("Unknown accuracy");
        }
    }

    public static DayPart hourOf(int hour) {
        for (DayPart part : values()) {
            if (part.start.getHourOfDay() == hour && part.accuracy == HOUR) {
                return part;
            }
        }
        throw new IllegalArgumentException("Unknown hour: " + hour);
    }

    public static DayPart quarterOf(int hour) {
        for (DayPart part : values()) {
            if (part.getStart().getHourOfDay() <= hour
                    && hour <= part.getEnd().getHourOfDay()
                    && part.accuracy == QUARTER) {
                return part;
            }
        }
        throw new IllegalArgumentException("Unknown part for hour: " + hour);
    }

    @Override
    public String toString() {
        return accuracy == HOUR ? start.toString("ha") : name();
    }
}
