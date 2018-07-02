package com.ewind.hl.model.event;

import android.support.annotation.NonNull;

import java.util.Calendar;

public class EventDayTime extends EventDayPart {

    private final int hour;

    public EventDayTime(int year, int month, int day, int hour) {
        super(year, month, day, hour);
        this.hour = hour;
    }

    public EventDayTime(Calendar calendar) {
        super(calendar);
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
    }

    @NonNull
    @Override
    public Calendar toCalendar() {
        Calendar calendar = super.toCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return calendar;
    }

    public int getHour() {
        return hour;
    }
}
