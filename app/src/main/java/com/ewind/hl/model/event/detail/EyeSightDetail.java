package com.ewind.hl.model.event.detail;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EyeSightDetail implements EventDetail {

    private final EyeSight left;
    private final EyeSight right;

    public static final class EyeSight implements Serializable {
        private final BigDecimal sphere;
        private final BigDecimal cylinder;
        private final Integer axis;

        @JsonCreator
        public EyeSight(
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
    }

    @JsonCreator
    public EyeSightDetail(
            @JsonProperty("left") EyeSight left,
            @JsonProperty("right") EyeSight right) {
        this.left = left;
        this.right = right;
    }

    public EyeSight getLeft() {
        return left;
    }

    public EyeSight getRight() {
        return right;
    }
}
