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

    @Override
    public Object getDescription() {
        return name().charAt(0) + name().substring(1, name().length()).toLowerCase();
    }

}
