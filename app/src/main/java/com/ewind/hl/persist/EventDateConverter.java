package com.ewind.hl.persist;

import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.EventDate;

import org.joda.time.LocalDate;

import java.util.Locale;

public class EventDateConverter {

    public static String serialize(EventDate date) {
        if (date == null) {
            return null;
        }

        String result = date.getLocalDate().toString("yyyy-MM-dd");

        int startHour = date.getDayPart().getStartHour();
        int period = date.getDayPart().getPeriodHours();

        result += String.format(Locale.getDefault(), "-%02d-%02d", startHour, period);

        return result;
    }

    public static EventDate deserialize(String value) {
        if (value == null) {
            return null;
        }

        String[] split = value.split("-");

        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        int firstHour = Integer.parseInt(split[3]);
        int period = Integer.parseInt(split[4]);

        DayPart dayPart = DayPart.valueOf(firstHour, period);

        return new EventDate(new LocalDate(year, month, day), dayPart);
    }
}
