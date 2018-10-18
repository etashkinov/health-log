package com.ewind.hl.ui.history;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventScoreComparator;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.ui.TintHelper;

import java.util.Collections;
import java.util.List;

public class HistoryBarItemDetailView extends HistoryItemDetailView {
    private ImageView barView;

    public HistoryBarItemDetailView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_history_item_bar, this);

        barView = findViewById(R.id.chart_bar);
    }

    public void setItem(List<HistoryChartAdapter.HistoryItem<?>> items, int position, boolean isSelected) {
        HistoryChartAdapter.HistoryItem<?> item = items.get(position);
        if (item.events.isEmpty()) {
            barView.setVisibility(View.INVISIBLE);
        } else {
            barView.setVisibility(View.VISIBLE);

            Score maxScore = getMaxScore(item);

            float scale = barView.getContext().getResources().getDisplayMetrics().density;
            int heightInPx = (int) ((maxScore.getValue() + 20) * scale);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, heightInPx);
            params.gravity = Gravity.CENTER;
            barView.setLayoutParams(params);

            TintHelper.setScoreTint(barView, maxScore);
        }
    }

    protected Score getMaxScore(HistoryChartAdapter.HistoryItem<?> item) {
        Event<?> maxEvent = Collections.min(item.events, new EventScoreComparator());
        return maxEvent.getScore();
    }
}
