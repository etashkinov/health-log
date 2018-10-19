package com.ewind.hl.ui.history;

import android.content.Context;
import android.widget.FrameLayout;

import com.ewind.hl.ui.history.chart.ChartItem;

public abstract class HistoryItemDetailView extends FrameLayout {
    public HistoryItemDetailView(Context context) {
        super(context);
    }

    public abstract void setItem(ChartItem item, boolean isSelected);
}
