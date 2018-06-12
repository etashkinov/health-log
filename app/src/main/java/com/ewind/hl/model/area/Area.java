package com.ewind.hl.model.area;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class Area {
    private final List<String> events;
    private final List<Area> parts;

    public Area(List<String> events, List<Area> children) {
        this.events = events == null ? emptyList() : unmodifiableList(events);
        this.parts = children == null ? emptyList() : unmodifiableList(children);
    }

    public List<String> getEvents() {
        return events;
    }

    public List<Area> getParts() {
        return parts;
    }
}
