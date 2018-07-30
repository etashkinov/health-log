package com.ewind.hl.model.event.detail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface EventDetail extends Serializable {
}
