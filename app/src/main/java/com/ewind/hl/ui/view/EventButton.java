package com.ewind.hl.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.ui.EventUI;
import com.ewind.hl.ui.LocalizationService;

public class EventButton extends LinearLayout {

    public interface OnEventClickListener {
        void onClick(EventType<?> type);
    }

    private EventType<?> type;

    public EventButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public <T extends EventDetail> void setEvent(EventType<T> type, T detail) {
        this.type = type;

        String text = getEventName(type, detail);
        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(text);

        ImageView eventImage = findViewById(R.id.eventImage);
        eventImage.setImageDrawable(getEventDrawable());
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        setOnClickListener(v -> listener.onClick(type));
    }

    private Drawable getEventDrawable() {
        return EventUI.getEventDrawable(type, getContext());
    }

    private String getEventName(EventType type, EventDetail detail) {
        return LocalizationService.getEventTypeName(type) + (detail == null ? "" : "!");
    }
}
