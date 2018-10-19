package com.ewind.hl.ui.history.chart;

import java.util.List;

public class ChartData {
    private final List<ChartItem> items;
    private final String highLabel;
    private final String lowLabel;

    public ChartData(List<ChartItem> items, String highLabel, String lowLabel) {
        this.items = items;
        this.highLabel = highLabel;
        this.lowLabel = lowLabel;
    }

    public List<ChartItem> getItems() {
        return items;
    }

    public String getHighLabel() {
        return highLabel;
    }

    public String getLowLabel() {
        return lowLabel;
    }
}
