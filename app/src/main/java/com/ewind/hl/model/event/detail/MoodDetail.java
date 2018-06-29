package com.ewind.hl.model.event.detail;

public enum MoodDetail implements EventDetail {
    DEPRESSED,
    SAD,
    NEUTRAL,
    GOOD,
    HAPPY;

    @Override
    public double getScore() {
        return (MoodDetail.values().length - (double) ordinal()) / MoodDetail.values().length * 100;
    }


}
