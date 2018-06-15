package com.ewind.hl.model.event.detail;

import com.ewind.hl.model.event.EventType;

import java.math.BigDecimal;

public class EyeSightDetail implements EventDetail {

    private final BigDecimal sphere;
    private final BigDecimal cylinder;
    private final int axis;

    public EyeSightDetail(BigDecimal sphere, BigDecimal cylinder, int axis) {
        this.sphere = sphere;
        this.cylinder = cylinder;
        this.axis = axis;
    }

    public BigDecimal getShepre() {
        return sphere;
    }

    public BigDecimal getCylinder() {
        return cylinder;
    }

    public int getAxis() {
        return axis;
    }

    @Override
    public EventType getType() {
        return EventType.EYE_SIGHT;
    }

    @Override
    public String toString() {
        return sphere + (cylinder == null ? "" : ", CYL " + cylinder + ", AX " + axis + "°");
    }
}
