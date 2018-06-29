package com.ewind.hl.model.event.detail;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public interface EventDetail extends Serializable {
    @JsonIgnore
    double getScore();
}
