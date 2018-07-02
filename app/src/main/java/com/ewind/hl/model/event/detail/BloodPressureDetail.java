package com.ewind.hl.model.event.detail;

public class BloodPressureDetail implements EventDetail {

    private final int highPressure;
    private final int lowPressure;

    public BloodPressureDetail(int highPressure, int lowPressure) {
        this.highPressure = highPressure;
        this.lowPressure = lowPressure;
    }

    public int getHighPressure() {
        return highPressure;
    }

    public int getLowPressure() {
        return lowPressure;
    }

    @Override
    public String toString() {
        return lowPressure + "x" + highPressure;
    }

    @Override
    public double getScore() {
        return (double) (highPressure + lowPressure) / 2;
    }

    @Override
    public Object getDescription() {
        return getScore();
    }
}
