package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

import java.math.BigDecimal;

public class HeartBeatDetail extends ValueDetail {

    public enum HeartBeatState {
        RELAXED,
        NORMAL,
        EXERCISE
    }

    private final HeartBeatState state;

    public HeartBeatDetail(int rate, HeartBeatState state) {
        super(EventType.HEART_BEAT, BigDecimal.valueOf(rate));
        this.state = state;
    }

    public HeartBeatState getState() {
        return state;
    }
}
