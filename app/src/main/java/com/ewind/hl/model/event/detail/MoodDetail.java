package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

public enum MoodDetail implements EventDetail {
    DEPRESSED,
    SAD,
    NEUTRAL,
    GOOD,
    HAPPY;

    @Override
    public EventType getType() {
        return EventType.MOOD;
    }

    @Override
    public double getScore() {
        return (MoodDetail.values().length - (double) ordinal()) / MoodDetail.values().length * 100;
    }


}
