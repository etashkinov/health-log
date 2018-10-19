package com.ewind.hl.ui.history.chart.period;

import android.support.annotation.NonNull;

import com.ewind.hl.model.event.EventDate;

public interface HistoryPeriod extends Comparable<HistoryPeriod> {
    String getLabel();
    String getShortLabel();

    boolean contains(EventDate eventDate);

    HistoryPeriod add(int steps);

    int minus(HistoryPeriod from);

    @Override
    default int compareTo(@NonNull HistoryPeriod period) {
        return minus(period);
    }
}
