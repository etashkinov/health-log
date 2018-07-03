package com.ewind.hl.persist;

import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.EventDate;

import org.joda.time.LocalDate;

import java.util.Locale;

import static java.lang.String.format;

public class EventDateConverter {
    public static String serialize(EventDate date) {
        if (date == null) {
            return null;
        }

        String result = date.getLocalDate().toString("yyyy-MM-dd");

        if (date.getHour() != null) {
            result += format(Locale.getDefault(), "-%s-%02d", date.getDayPart(), date.getHour());
        } else if (date.getDayPart() != null) {
            result += "-" + date.getDayPart();
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

        DayPart dayPart = null;
        Integer hour = null;

        if (split.length > 3) {
            dayPart = DayPart.valueOf(split[3]);

            if (split.length > 4) {
                hour = Integer.parseInt(split[4]);
            }
        }

        return new EventDate(new LocalDate(year, month, day), dayPart, hour);
    }
}
