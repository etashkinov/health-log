package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;
import com.fasterxml.jackson.annotation.JsonIgnore;

public interface EventDetail {

    @JsonIgnore
    EventType getType();

    @JsonIgnore
    double getScore();
}
