package com.ewind.hl.model.event.detail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EyeSightDetail implements EventDetail {

    private final BigDecimal sphere;
    private final BigDecimal cylinder;
    private final Integer axis;

    @JsonCreator
    public EyeSightDetail(
            @JsonProperty("sphere") BigDecimal sphere,
            @JsonProperty("cylinder") BigDecimal cylinder,
            @JsonProperty("axis") Integer axis) {
        this.sphere = sphere;
        this.cylinder = cylinder;
        this.axis = axis;
    }

    public BigDecimal getSphere() {
        return sphere;
    }

    public BigDecimal getCylinder() {
        return cylinder;
    }

    public Integer getAxis() {
        return axis;
    }

    @Override
    public double getScore() {
        return (sphere == null ? BigDecimal.ZERO : sphere).abs()
                .add(cylinder == null ? BigDecimal.ZERO : cylinder.abs())
                .doubleValue();
    }

    @Override
    public String toString() {
        return sphere + (cylinder == null ? "" : ", CYL " + cylinder + ", AX " + axis + "°");
    }
}
