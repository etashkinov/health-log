package com.ewind.hl.ui.history.chart;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.ui.TintHelper;
import com.ewind.hl.ui.history.HistoryItemDetailView;

public class HistoryLineItemDetailView extends HistoryItemDetailView {

    public static final String TAG = HistoryLineItemDetailView.class.getSimpleName();

    private final ImageView canvasView;
    private final ImageView dotView;

    public HistoryLineItemDetailView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.view_history_item_line, this);

        dotView = findViewById(R.id.history_item_line_dot);
        canvasView = findViewById(R.id.history_item_line_canvas);
    }

    public void setItem(ChartItem item, boolean isSelected) {
        if (item.getValue() == 0) {
            dotView.setVisibility(View.INVISIBLE);
            canvasView.setVisibility(View.INVISIBLE);
        } else {
            Context context = dotView.getContext();
            dotView.setVisibility(View.VISIBLE);
            canvasView.setVisibility(View.VISIBLE);

            int value = item.getValue();

            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    (ConstraintLayout.LayoutParams)dotView.getLayoutParams());

            Event event = item.getEvent();
            if (event != null) {
                dotView.setImageDrawable(context.getDrawable(R.drawable.ic_event_dot));
                params.bottomMargin = getValueInPx(value, context);
                TintHelper.setScoreTint(dotView, event.getScore());
            } else {
                dotView.setImageDrawable(context.getDrawable(R.drawable.ic_dot));
                params.bottomMargin = getValueInPx(value + 9, context);
                TintHelper.setTint(dotView, ContextCompat.getColor(context, android.R.color.darker_gray));
            }

            dotView.setLayoutParams(params);
            dotView.getParent().requestLayout();
        }
    }

    protected int getValueInPx(int value, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (value * scale);
    }
}
