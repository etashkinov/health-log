package com.ewind.hl.ui.event.def;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ewind.hl.R;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EnumEventType;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.event.EventFormView;
import com.ewind.hl.ui.view.EventDatePicker;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.area.AreaSelector;
import com.ewind.hl.ui.view.detail.GenericDetailForm;

public class DefaultEventFormView <D extends EventDetail> extends LinearLayout implements EventFormView<D> {

    private static final String TAG = DefaultEventFormView.class.getName();

    private long eventId;
    private EventType<D> type;

    private EventDetailForm<D> detailForm;
    private EventDatePicker eventDatePicker;
    private AreaSelector areaSelector;
    private EditText noteText;

    public DefaultEventFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        eventDatePicker = findViewById(R.id.eventDatePicker);
        eventDatePicker.setListener(d -> {});

        areaSelector = findViewById(R.id.areaSelector);
        noteText = findViewById(R.id.noteText);
    }

    public EventType<D> getType() {
        return type;
    }

    @Override
    public void setEvent(Event<D> event) {
        eventId = event.getId();
        type = event.getType();

        eventDatePicker.setDate(event.getDate());
        areaSelector.setArea(event.getType(), event.getArea());

        initDetailForm(event);

        noteText.setText(event.getNote());
    }

    private void initDetailForm(Event<D> event) {
        detailForm = getEventDetailForm(event, getContext());
        ((ViewGroup) findViewById(R.id.eventDetailContainer)).addView((View) detailForm);

        if (event.getDetail() != null) {
            detailForm.setDetail(event.getDetail());
        }
    }

    protected EventDetailForm<D> getEventDetailForm(Event<D> event, Context context) {
        EventType<D> type = event.getType();
        try {
            String name = "event_" + type.getName().toLowerCase() + "_form";
            int layout = context.getResources().getIdentifier(name, "layout", context.getPackageName());
            return (EventDetailForm<D>) LayoutInflater.from(context).inflate(layout, null);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Detail form layout for " + type + " not found");
            int eventDetailLayoutId = getDefaultEventDetailLayoutId(type);
            GenericDetailForm<D> defaultForm = (GenericDetailForm<D>) LayoutInflater.from(context).inflate(eventDetailLayoutId, null);
            defaultForm.setEventType(type);
            return defaultForm;
        }
    }

    private static int getDefaultEventDetailLayoutId(EventType type) {
        return type instanceof EnumEventType
                ? R.layout.event_enum_form
                : R.layout.event_value_form;
    }

    @Override
    public Event<D> getEvent() {
        return new Event<>(
                eventId,
                eventDatePicker.getDate(),
                type,
                detailForm.getDetail(),
                areaSelector.getArea() == null ? AreaFactory.getBody() : areaSelector.getArea(),
                noteText.getText().toString(),
                type.getScore(detailForm.getDetail()));
    }
}
