package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

public class HeartBeatDetail implements EventDetail {

    public enum HeartBeatState {
        RELAXED,
        NORMAL,
        EXERCISE
    }

    private final int rate;
    private final HeartBeatState state;

    public HeartBeatDetail(int rate, HeartBeatState state) {
        this.rate = rate;
        this.state = state;
    }

    public int getRate() {
        return rate;
    }

    public HeartBeatState getState() {
        return state;
    }

    @Override
    public EventType getType() {
        return EventType.HEART_BEAT;
    }
}
