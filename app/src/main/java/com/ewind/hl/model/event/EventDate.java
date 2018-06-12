package com.ewind.hl.model.event;

import java.io.Serializable;

public class EventDate implements Serializable {
    private final int year;
    private final int month;
    private final int day;

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
}
