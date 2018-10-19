package com.ewind.hl.ui.history.chart;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.ui.TintHelper;
import com.ewind.hl.ui.history.HistoryItemDetailView;

public class HistoryBarItemDetailView extends HistoryItemDetailView {
    private ImageView barView;

    public HistoryBarItemDetailView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_history_item_bar, this);

        barView = findViewById(R.id.chart_bar);
    }

    public void setItem(ChartItem item, boolean isSelected) {
        if (item.getEvent() == null) {
            barView.setVisibility(View.INVISIBLE);
        } else {
            barView.setVisibility(View.VISIBLE);

            float scale = barView.getContext().getResources().getDisplayMetrics().density;
            int heightInPx = (int) ((item.getValue() + 20) * scale);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, heightInPx);
            params.gravity = Gravity.CENTER;
            barView.setLayoutParams(params);

            TintHelper.setScoreTint(barView, item.getEvent().getScore());
        }
    }
}
