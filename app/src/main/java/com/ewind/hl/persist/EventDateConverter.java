package com.ewind.hl.persist;

import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventDayPart;
import com.ewind.hl.model.event.EventDayTime;

import java.util.Locale;

import static java.lang.String.format;

public class EventDateConverter {
    public static String serialize(EventDate date) {
        if (date == null) {
            return null;
        }

        String result = format(Locale.getDefault(), "%04d-%02d-%02d", date.getYear(), date.getMonth(), date.getDay());

        if (date instanceof EventDayTime) {
            result += format(Locale.getDefault(), "-%02d", ((EventDayTime)date).getHour());
        } else if (date instanceof EventDayPart) {
            result += "-" + ((EventDayPart)date).getDayPart();
        }
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

        if (split.length == 3) {
            return new EventDate(year, month, day);
        } else try {
            int hour = Integer.parseInt(split[3]);
            return new EventDayTime(year, month, day, hour);
        } catch (NumberFormatException e) {
            return new EventDayPart(year, month, day, EventDayPart.DayPart.valueOf(split[3]));
        }
    }
}
