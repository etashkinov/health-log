package com.ewind.hl.persist;

import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.EventDate;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import static com.ewind.hl.model.event.EventDate.DAY;
import static com.ewind.hl.model.event.EventDate.HOUR;

public class EventDateConverter {

    public static String serialize(EventDate date) {
        if (date == null) {
            return null;
        }

        String result = date.getLocalDate().toString("yyyy-MM-dd");

        Period period = date.getDayPart().getPeriod();
        int startHour = date.getStart().getHourOfDay();
        if (period == HOUR) {
            result += "-" + DayPart.quarterOf(startHour).getStart().getHourOfDay();
        }

        if (period != DAY) {
            result += "-" + startHour;
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

        DayPart dayPart = getDayPart(value, split);

        return new EventDate(new LocalDate(year, month, day), dayPart);
    }

    private static DayPart getDayPart(String value, String[] split) {
        switch (split.length) {
            case 3: return DayPart.ALL_DAY;
            case 4: return DayPart.quarterOf(Integer.parseInt(split[3]));
            case 5: return DayPart.hourOf(Integer.parseInt(split[4]));
            default:
                throw new IllegalArgumentException("Unknown day part for " + value);
        }
    }
}
