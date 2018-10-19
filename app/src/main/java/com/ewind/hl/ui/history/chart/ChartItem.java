package com.ewind.hl.ui.history.chart;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.ui.history.chart.period.HistoryPeriod;

public class ChartItem {
    private final HistoryPeriod period;
    private final int value;
    private final String label;
    private final Event event;

    public ChartItem(HistoryPeriod period, int value, String label, Event event) {
        this.period = period;
        this.value = value;
        this.label = label;
        this.event = event;
    }

    public HistoryPeriod getPeriod() {
        return period;
    }

    public int getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public Event getEvent() {
        return event;
    }
}
