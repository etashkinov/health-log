package com.ewind.hl.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.ScoreBand;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;

public class EventUI {

    private static final String TAG = EventUI.class.getName();
    private static final int DEFAULT_ICON = R.drawable.ic_event;

    public static Drawable getEventTypeDrawable(EventType type, Context context) {
        int id = getEventDrawableId(type, context);
        return context.getDrawable(id);
    }

    public static boolean isDefaultIcon(EventType type, Context context) {
        int id = getEventDrawableId(type, context);
        return id == DEFAULT_ICON;
    }

    private static int getEventDrawableId(EventType type, Context context) {
        String name = "ic_" + type.getName().toLowerCase();
        int result = getDrawableByName(name, context);
        if (result == 0) {
            Log.w(TAG, "Icon for " + type.getName() + " not found");
            result = DEFAULT_ICON;
        }
        return result;
    }

    public static int getDrawableByName(String name, Context context) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    public static <D extends EventDetail> String getEventDescription(Event<D> event, Context context) {
        return event.getType().getDescription(event, context);
    }

    public static int getEventTint(Event event, Context context) {
        Score score = event.getScore();
        int band = new ScoreBand(score).getBand();
        int result = context.getResources().getIdentifier("severity" + band, "color", context.getPackageName());
        return result == 0 ? R.color.colorAccent : result;
    }
}
