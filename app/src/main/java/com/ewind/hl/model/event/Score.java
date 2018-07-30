package com.ewind.hl.model.event;

import java.io.Serializable;
import java.math.BigDecimal;

public class Score implements Serializable {
    public static final int MAX = 100;
    public static final int MIN = 0;
    public static final int RANGE = MAX - MIN;

    private final int value;

    public Score(int score) {
        if (score > MAX) {
            throw new IllegalArgumentException("Score is too high: " + score);
        }

        if (score < MIN) {
            throw new IllegalArgumentException("Score is too low: " + score);
        }

        this.value = score;
    }

    public Score(BigDecimal percent) {
        this((int) (percent.doubleValue() * RANGE), MAX);
    }

    public Score(int score, int scale) {
        this((int) (score * RANGE / (double) scale));
    }

    public boolean isNormal() {
        return value == MIN;
    }

    public int getValueAtScale(int scale) {
        return (int) (value * (double) scale / RANGE);
    }

    public int getValue() {
        return value;
    }
}
