package com.ewind.hl.ui.history.chart;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.ui.UiHelper;
import com.ewind.hl.ui.event.EventUI;
import com.ewind.hl.ui.event.EventUIFactory;
import com.ewind.hl.ui.history.HistoryItemDetailView;

public class HistoryBarItemDetailView extends HistoryItemDetailView {
    private ImageView barView;

    public HistoryBarItemDetailView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_history_item_bar, this);

        barView = findViewById(R.id.chart_bar);
    }

    public void setItem(ChartItem item, boolean isSelected) {
        Event event = item.getEvent();
        if (event == null) {
            barView.setVisibility(View.INVISIBLE);
        } else {
            barView.setVisibility(View.VISIBLE);

            Context context = barView.getContext();
            float scale = context.getResources().getDisplayMetrics().density;
            int heightInPx = (int) ((item.getValue() + 20) * scale);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, heightInPx);
            params.gravity = Gravity.CENTER;
            barView.setLayoutParams(params);

            EventUI ui = EventUIFactory.getUI(event.getType(), context);
            UiHelper.setTint(barView, ui.getEventTint(event));
        }
    }
}
