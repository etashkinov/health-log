package com.ewind.hl.model.event;

import java.util.Arrays;

public class ScoreBand {
    public static final int BANDS_NUMBER = 6;
    private final Score score;

    public static ScoreBand of(int band) {
        if (band >= BANDS_NUMBER) {
            throw new IllegalArgumentException("Band should be less than " + BANDS_NUMBER + ": " + band);
        }

        return new ScoreBand(new Score(band, BANDS_NUMBER));
    }

    public ScoreBand(int score) {
        this(new Score(score));
    }

    public ScoreBand(Score score) {
        this.score = score;
    }

    public int getScore() {
        return score.getValue();
    }

    public int getBand() {
        return score.getValueAtScale(BANDS_NUMBER);
    }

    public <E extends Enum<E>> E getType(Class<E> enumClass) {
        E[] enumConstants = enumClass.getEnumConstants();
        if (enumConstants.length != BANDS_NUMBER) {
            throw new IllegalArgumentException("Enum " + enumClass + " must contain exactly " +
                    BANDS_NUMBER + " constants: " + Arrays.toString(enumConstants));
        }

        return enumConstants[getBand()];
    }
}
