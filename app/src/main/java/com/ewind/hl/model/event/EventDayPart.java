package com.ewind.hl.model.event;

import android.support.annotation.NonNull;

import java.util.Calendar;

public class EventDayPart extends EventDate {

    public enum DayPart {
        NIGHT(6),
        MORNING(12),
        AFTERNOON(18),
        EVENING(24);

        private final int lastHour;

        DayPart(int lastHour) {
            this.lastHour = lastHour;
        }

        static DayPart of(int hour) {
            for (DayPart part : DayPart.values()) {
                if (part.lastHour < hour) {
                    return part;
                }
            }
            throw new IllegalArgumentException("Unknown hour: " + hour);
        }
    }

    private final DayPart dayPart;

    public EventDayPart(int year, int month, int day, int hour) {
        this(year, month, day, DayPart.of(hour));
    }

    public EventDayPart(int year, int month, int day, DayPart dayPart) {
        super(year, month, day);
        this.dayPart = dayPart;
    }

    public EventDayPart(Calendar calendar) {
        super(calendar);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.dayPart = DayPart.of(hour);
    }

    @NonNull
    @Override
    public Calendar toCalendar() {
        Calendar calendar = super.toCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, dayPart.lastHour - 6); //FIXME
        return calendar;
    }

    public DayPart getDayPart() {
        return dayPart;
    }
}
