package com.ewind.hl.ui.event;

import android.content.Context;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.MeasurementEventType;
import com.ewind.hl.ui.history.HistoryItemDetailView;
import com.ewind.hl.ui.history.chart.ChartData;
import com.ewind.hl.ui.history.chart.ChartItem;
import com.ewind.hl.ui.history.chart.HistoryLineItemDetailView;
import com.ewind.hl.ui.history.chart.period.HistoryPeriod;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MeasurementEventUI<D extends ValueDetail, T extends MeasurementEventType<D>> extends DefaultEventUI<D, T> {

    public MeasurementEventUI(EventType<D> type, Context context) {
        super(type, context);
    }

    @Override
    protected int getDefaultEventDetailLayoutId() {
        return R.layout.event_measurement_form;
    }

    @Override
    public HistoryItemDetailView createHistoryItemDetail(Context context) {
        return new HistoryLineItemDetailView(context);
    }

    @Override
    public ChartData toChartData(Map<HistoryPeriod, List<Event<D>>> historyItems, HistoryPeriod from, HistoryPeriod till) {
        int steps = till.minus(from);
        ArrayList<ChartItem> items = new ArrayList<>(steps);

        BigDecimal low = null;
        BigDecimal high = null;
        Map<HistoryPeriod, Event<D>> values = new HashMap<>(historyItems.size());

        for (Map.Entry<HistoryPeriod, List<Event<D>>> entry : historyItems.entrySet()) {
            Event<D> latestEvent = entry.getValue().get(0);
            BigDecimal latestValue = getValue(latestEvent);
            if (low == null || latestValue.compareTo(low) < 0) {
                low = latestValue;
            }
            if (high == null || latestValue.compareTo(high) > 0) {
                high = latestValue;
            }
            values.put(entry.getKey(), latestEvent);
        }

        BigDecimal range = high.subtract(low);
        BigDecimal padding = range.multiply(BigDecimal.valueOf(0.1)).add(BigDecimal.ONE);
        low = low.subtract(padding);
        high = high.add(padding);
        range = range.add(padding).add(padding);

        for (int i = 0; i <= steps; i++) {
            HistoryPeriod period = from.add(i);
            Event<D> latestEvent = values.get(period);

            if (latestEvent == null) {
                items.add(new ChartItem(period, 0, "", latestEvent));
            } else {
                String description = latestEvent.getType().getDescription(latestEvent, context.get());

                int value = getValue(latestEvent).subtract(low)
                        .divide(range, 1, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100))
                        .intValue();

                items.add(new ChartItem(period, value, description, latestEvent));
            }
        }
        String lowLabel = type.getDescription(type.createDetail(low), context.get());
        String highLabel = type.getDescription(type.createDetail(high), context.get());
        return new ChartData(items, highLabel, lowLabel);
    }

    protected BigDecimal getValue(Event<D> latestEvent) {
        return latestEvent.getDetail().getValue();
    }
}
