package com.ewind.hl.ui;

import android.support.annotation.NonNull;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Accuracy;
import com.ewind.hl.model.event.DayPart;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Months;
import org.joda.time.Years;

import static com.ewind.hl.model.event.Accuracy.HOUR;

public class LocalizationService {

    public static String getAreaName(Area area) {
        String name = area.getName();
        return getAreaName(name);
    }

    @NonNull
    public static String getAreaName(String name) {
        return snakeCaseToReadable(name);
    }

    public static String getEventTypeName(EventType type) {
        return getAreaName(type.getName().toLowerCase());
    }

    public static String getEventDate(EventDate date) {
        String result = getLocalDate(date.getLocalDate());

        DayPart part = date.getDayPart();
        if (part != DayPart.ALL_DAY) {
            result += " " + getDayPart(part);
        }

        return result;
    }

    public static String getLocalDate(LocalDate localDate) {
        return localDate.toString("dd MMM yy");
    }

    public static String getEventDateFrom(EventDate date, LocalDateTime from) {
        DayPart dayPart = date.getDayPart();
        LocalDate localDate = date.getLocalDate();
        LocalDate fromDate = from.toLocalDate();

        int daysAgo = Days.daysBetween(localDate, fromDate).getDays();
        int monthsAgo = Months.monthsBetween(localDate, fromDate).getMonths();
        int yearsAgo = Years.yearsBetween(localDate, fromDate).getYears();

        Accuracy accuracy = dayPart.getAccuracy();
        if (daysAgo == 0) {
            switch (accuracy) {
                case DAY: return "Today";
                case QUARTER: return "This " + getDayPart(dayPart).toLowerCase();
                case HOUR: return from.getHourOfDay() - dayPart.getStart().getHourOfDay() + " hours ago";
                default: throw new IllegalArgumentException("Unknown accuracy " + accuracy);
            }
        } else if (daysAgo == 1) {
            switch (accuracy) {
                case DAY: return "Yesterday";
                case QUARTER: return "Yesterdays " + getDayPart(dayPart).toLowerCase();
                case HOUR: return "Yesterday " + getDayPart(dayPart);
                default: throw new IllegalArgumentException("Unknown accuracy " + accuracy);
            }
        } else if (monthsAgo < 1) {
            return daysAgo + " days ago";
        } else if (monthsAgo == 1) {
            return monthsAgo + " month ago";
        } else if (yearsAgo < 1) {
            return monthsAgo + " months ago";
        } else if (yearsAgo == 1) {
            return yearsAgo + " year ago";
        } else {
            return yearsAgo + " years ago";
        }
    }

    public static String getDayPart(DayPart part) {
        String dayPart;
        if (part.getAccuracy() == HOUR) {
            dayPart = part.getStart().toString("haa");
        } else {
            dayPart = snakeCaseToReadable(part.name());
        }
        return dayPart;
    }

    @NonNull
    private static String snakeCaseToReadable(String string) {
        String title = string.toLowerCase().replaceAll("_", " ");
        return Character.toUpperCase(title.charAt(0)) + title.substring(1);
    }
}
