package com.ewind.hl.model.event;

public class Duration {
    private final int value;
    private final ChronoUnit unit;

    public static Duration of(int value, ChronoUnit unit) {
        return new Duration(value, unit);
    }

    private Duration(int value, ChronoUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    public long getDuration() {
        return value * unit.getDuration();
    }
}
