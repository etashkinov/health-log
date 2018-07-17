package com.ewind.hl.model.event.detail;

public enum MoodDetail implements EventDetail {
    DEPRESSED,
    SAD,
    NEUTRAL,
    GOOD,
    HAPPY;

    @Override
    public double getScore() {
        return (values().length - (double) ordinal()) / values().length * 100;
    }
}
