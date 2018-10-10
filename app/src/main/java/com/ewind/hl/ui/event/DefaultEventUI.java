package com.ewind.hl.ui.event;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.ScoreBand;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.ui.LocalizationService;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.detail.GenericDetailForm;

public class DefaultEventUI<D extends EventDetail, T extends EventType<D>> implements EventUI<D> {
    private static final String TAG = EventUI.class.getName();
    private static final int DEFAULT_ICON = R.drawable.ic_event;

    private final T type;
    public DefaultEventUI(EventType<D> type) {
        this.type = (T) type;
    }

    @Override
    public EventDetailForm<D> getEventDetailForm(ViewGroup parent) {
        Context context = parent.getContext();
        try {
            String name = "event_" + type.getName().toLowerCase() + "_form";
            int layout = context.getResources().getIdentifier(name, "layout", context.getPackageName());
            return (EventDetailForm<D>) LayoutInflater.from(context).inflate(layout, parent, false);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Detail form layout for " + type + " not found");
            int eventDetailLayoutId = getDefaultEventDetailLayoutId();
            GenericDetailForm<D, T> defaultForm = (GenericDetailForm<D, T>) LayoutInflater.from(context).inflate(eventDetailLayoutId, parent, false);
            defaultForm.setEventType(type);
            return defaultForm;
        }
    }

    protected int getDefaultEventDetailLayoutId() {
        return R.layout.event_value_form;
    }

    @Override
    public Drawable getEventTypeDrawable(Context context) {
        int id = getEventDrawableId(context);
        return context.getDrawable(id);
    }

    @Override
    public int getEventDrawableId(Context context) {
        String name = "ic_" + type.getName().toLowerCase();
        int result = getDrawableByName(name, context);
        if (result == 0) {
            Log.w(TAG, "Icon for " + type.getName() + " not found");
            result = DEFAULT_ICON;
        }
        return result;
    }

    @Override
    public int getDrawableByName(String name, Context context) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    @Override
    public String getEventDescription(Event<D> event, Context context) {
        return event.getType().getDescription(event, context);
    }

    @Override
    public View getLastEventDetailView(Event<D> event, ViewGroup parent) {
        Context context = parent.getContext();
        int viewLayoutId = getDefaultLastEventDetailLayoutId();

        View view = LayoutInflater.from(context).inflate(viewLayoutId, parent, false);

        String text = LocalizationService.getEventTypeName(context, event.getType());
        if (EventTypeFactory.getAreas(event.getType()).size() > 1) {
            text = LocalizationService.getAreaName(event.getArea()) + " " + text;
        }

        TextView eventTypeText = view.findViewById(R.id.eventTypeText);
        if (eventTypeText != null) {
            eventTypeText.setText(text);
        }

        TextView eventDetailText = view.findViewById(R.id.eventDetailText);

        if (eventDetailText != null) {
            eventDetailText.setText(getEventDescription(event, context));
        }

        return view;
    }

    @Override
    public View getHistoryEventDetailView(Event event, ViewGroup parent) {
        TextView result = new TextView(parent.getContext());
        String eventDescription = getEventDescription(event, parent.getContext());
        result.setText(eventDescription);
        return result;
    }

    protected int getDefaultLastEventDetailLayoutId() {
        return R.layout.event_item_detail;
    }

    @Override
    public void setEventTint(ImageView image, Event event) {
        Context context = image.getContext();
        int eventTint = getEventTint(event, context);
        if (eventTint != 0) {
            int color = ContextCompat.getColor(context, eventTint);
            image.setImageTintList(ColorStateList.valueOf(color));
            image.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
        }
    }

    private int getEventTint(Event event, Context context) {
        int band = getBand(event);
        int result = context.getResources().getIdentifier("severity" + band, "color", context.getPackageName());
        return result == 0 ? R.color.colorAccent : result;
    }

    private int getBand(Event event) {
        Score score = event.getScore();
        return new ScoreBand(score).getBand();
    }
}
