package com.ewind.hl.model.area;

import android.support.annotation.NonNull;

import com.ewind.hl.model.event.EventType;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class Area {

    private static int areaId = 0;

    private final int id;
    private final String name;
    private final List<EventType> events;
    private final List<Area> parts;

    private Area parent;

    public Area(String name, List<EventType> events, List<Area> children) {
        this.id = areaId++;
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
        return name + "-e-" + events.size() + "-p-" + parts.size();
    }

    public Area getParent() {
        return parent;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getDescription() {
        String title = getName().replaceAll("_"," ");
        return Character.toUpperCase(title.charAt(0)) + title.substring(1);
    }
}
