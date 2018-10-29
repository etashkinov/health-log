package com.ewind.hl.ui.event;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.history.HistoryItemDetailView;
import com.ewind.hl.ui.history.chart.ChartData;
import com.ewind.hl.ui.history.chart.period.HistoryPeriod;
import com.ewind.hl.ui.view.EventDetailForm;

import java.util.List;
import java.util.Map;

public interface EventUI<D extends EventDetail> {
    EventDetailForm<D> getEventDetailForm(ViewGroup parent);

    Drawable getEventTypeDrawable();

    int getEventDrawableId();

    int getDrawableByName(String name);

    String getEventDescription(Event<D> event);

    View getLastEventDetailView(Event<D> event);

    View getHistoryEventDetailView(Event event, ViewGroup parent);

    int getEventTint(Event event);

    HistoryItemDetailView createHistoryItemDetail();

    ChartData toChartData(Map<HistoryPeriod, List<Event<D>>> historyItems,
                          HistoryPeriod from, HistoryPeriod till);
}
