package com.ewind.hl.model.event;

public enum ChronoUnit {
    MILLIS(1),
    SECONDS(MILLIS.duration * 1000),
    MINUTES(SECONDS.duration * 60),
    HOURS(MINUTES.duration * 60),
    QUARTER(HOURS.duration * 8),
    DAYS(HOURS.duration * 24),
    WEEKS(DAYS.duration * 7),
    MONTHS(DAYS.duration * 30),
    YEARS(DAYS.duration * 365),
    FOREVER(-1);

    private final long duration;

    ChronoUnit(long duration) {
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }
}