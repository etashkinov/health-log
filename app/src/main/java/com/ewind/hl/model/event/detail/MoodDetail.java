package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

@EventType("mood")
public enum MoodDetail implements EventDetail {

    DEPRESSED,
    SAD,
    NEUTRAL,
    GOOD,
    HAPPY

}
