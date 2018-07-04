package com.ewind.hl.model.area;

import com.ewind.hl.model.event.EventType;

import java.io.Serializable;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class Area implements Serializable {

    private final String name;
    private final List<EventType> events;
    private final List<Area> parts;

    private Area parent;

    public Area(String name, List<EventType> events, List<Area> children) {
        this.name = name;
        this.events = events == null ? emptyList() : unmodifiableList(events);
        this.parts = children == null ? emptyList() : unmodifiableList(children);
        for (Area part : this.parts) {
            part.parent = this;
        }
    }

    public String getName() {
        return name;
    }

    public List<EventType> getEvents() {
        return events;
    }

    public List<Area> getParts() {
        return parts;
    }

    @Override
    public String toString() {
        return name;
    }

    public Area getParent() {
        return parent;
    }
}
