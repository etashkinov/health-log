package com.ewind.hl.ui.history;

import android.content.Context;
import android.widget.FrameLayout;

import java.util.List;

public abstract class HistoryItemDetailView extends FrameLayout {
    public HistoryItemDetailView(Context context) {
        super(context);
    }

    public abstract void setItem(List<HistoryChartAdapter.HistoryItem<?>> items, int position, boolean isSelected);
}
