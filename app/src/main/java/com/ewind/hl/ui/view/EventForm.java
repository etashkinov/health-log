package com.ewind.hl.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class EventForm extends RelativeLayout {

    private View detailForm;

    public EventForm(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventDetail getEventDetail() {
        return ((EventDetailForm)detailForm).getDetail();
    }

    public void setEventDetail(EventType type, EventDetail detail) {
        initHeader(type);
        initDetailForm(type, detail);
    }

    private void initHeader(EventType type) {
        ImageView eventImage = findViewById(R.id.eventImage);
        eventImage.setImageDrawable(getEventDrawable(type));

        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(type.name());
    }

    private Drawable getEventDrawable(EventType type) {
        return getContext().getDrawable(android.R.drawable.ic_menu_mylocation);
    }

    private void initDetailForm(EventType type, EventDetail detail) {
        int layoutId = getEventFormLayoutId(type);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        detailForm = inflater.inflate(layoutId, null);
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        params.addRule(BELOW, R.id.eventHeader);
        detailForm.setLayoutParams(params);
        addView(detailForm);

        if (detail != null) {
            ((EventDetailForm) detailForm).setDetail(detail);
        }
    }


    private int getEventFormLayoutId(EventType type) {
        String name = "event_" + type.name().toLowerCase() + "_form";
        return getContext().getResources().getIdentifier(name, "layout", getContext().getPackageName());
    }

}
