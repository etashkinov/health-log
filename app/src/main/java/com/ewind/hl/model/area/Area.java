package com.ewind.hl.model.area;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.Collections.unmodifiableList;

public class Area implements Serializable {

    private final String name;
    private final List<Area> parts;

    private Area parent;

    public Area(String name, List<Area> children) {
        this.name = name;
        this.parts = children == null ? emptyList() : unmodifiableList(children);
        for (Area part : this.parts) {
            part.parent = this;
        }
    }

    public String getName() {
        return name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Area area = (Area) o;
        return Objects.equals(name, area.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
