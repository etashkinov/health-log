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

public class HistoryLineItemDetailView extends HistoryItemDetailView {
    private ImageView dotView;

    public HistoryLineItemDetailView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_history_item_line, this);

        dotView = findViewById(R.id.chart_dot);
    }

    public void setItem(ChartItem item, boolean isSelected) {
        if (item.getEvent() == null) {
            dotView.setVisibility(View.INVISIBLE);
        } else {
            dotView.setVisibility(View.VISIBLE);

            float scale = dotView.getContext().getResources().getDisplayMetrics().density;
            int valueInDp = (int) (item.getValue() * scale);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            params.bottomMargin = valueInDp;
            dotView.setLayoutParams(params);

            TintHelper.setScoreTint(dotView, item.getEvent().getScore());
        }
    }
}
