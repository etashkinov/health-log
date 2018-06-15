package com.ewind.hl.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;

public class EventButton extends LinearLayout {

    public interface OnEventClickListener {
        void onClick(View v, EventType type, EventDetail detail);
    }

    private EventType type;
    private EventDetail detail;

    public EventButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setEvent(EventType type, EventDetail detail) {
        this.type = type;
        this.detail = detail;


        String text = getEventName();
        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(text);

        ImageView eventImage = findViewById(R.id.eventImage);
        eventImage.setImageDrawable(getEventDrawable());
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        setOnClickListener(v -> listener.onClick(v, type, detail));
    }

    private Drawable getEventDrawable() {
        return getContext().getDrawable(android.R.drawable.ic_menu_mylocation);
    }

    private String getEventName() {
        return type + (detail == null ? "" : "+");
    }
}
