package com.ewind.hl.model.event;

public enum DayPart {
    NIGHT(0, 6),
    MORNING(6, 12),
    AFTERNOON(12, 18),
    EVENING(18, 24);

    private final int firstHour;
    private final int lastHour;

    DayPart(int firstHour, int lastHour) {
        this.firstHour = firstHour;
        this.lastHour = lastHour;
    }

    static DayPart of(int hour) {
        for (DayPart part : values()) {
            if (part.lastHour < hour) {
                return part;
            }
        }
        throw new IllegalArgumentException("Unknown hour: " + hour);
    }

    public int getFirstHour() {
        return firstHour;
    }

    public int getLastHour() {
        return lastHour;
    }
}
