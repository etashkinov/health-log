package com.ewind.hl.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.ui.model.EventModel;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.detail.ValueDetailForm;

public class EventUI {

    private static final String TAG = EventUI.class.getName();
    private static final int DEFAULT_ICON = R.drawable.ic_event;

    public static EventDetailForm getEventDetailForm(EventModel event, Context context) {
        EventType type = event.getType();
        try {
            String name = "event_" + type.getName().toLowerCase() + "_form";
            int layout = context.getResources().getIdentifier(name, "layout", context.getPackageName());
            return (EventDetailForm) LayoutInflater.from(context).inflate(layout, null);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Detail form layout for " + type + " not found");
            ValueDetailForm valueDetailForm = (ValueDetailForm) LayoutInflater.from(context).inflate(R.layout.event_value_form, null);
            valueDetailForm.setEventType(type);
            return valueDetailForm;
        }
    }

    public static Drawable getEventDrawable(EventType type, Context context) {
        int id = getEventDrawableId(type, context);
        return context.getDrawable(id);
    }

    public static boolean isDefaultIcon(EventType type, Context context) {
        int id = getEventDrawableId(type, context);
        return id == DEFAULT_ICON;
    }

    private static int getEventDrawableId(EventType type, Context context) {
        String name = "ic_" + type.getName().toLowerCase();
        int result = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        if (result == 0) {
            Log.w(TAG, "Icon for " + type.getName() + " not found");
            result = DEFAULT_ICON;
        }
        return result;
    }

    public static String getEventDescription(Event event, Context context) {
        String name = "event_description_" + event.getType().getName().toLowerCase();
        Object description = event.getDetail().getDescription();
        try {
            String template = context.getString(context.getResources().getIdentifier(name, "string", context.getPackageName()));
            return String.format(template, description);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Description for " + event.getType() + " not found");
            return String.format("%.1f", description);
        }
    }
}
