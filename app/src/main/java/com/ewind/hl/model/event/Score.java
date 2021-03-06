package com.ewind.hl.model.event;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

public class Score implements Serializable, Comparable<Score> {
    public static final int MAX = 100;
    public static final int MIN = 0;
    private static final double RANGE = MAX - MIN;

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
        this((int) (score * RANGE / (scale - 1)));
    }

    public boolean isNormal() {
        return value == MIN;
    }

    public int getValueAtScale(int scale) {
        return (int) (value * (scale - 1) / RANGE);
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(@NonNull Score score) {
        return value - score.value;
    }
}
