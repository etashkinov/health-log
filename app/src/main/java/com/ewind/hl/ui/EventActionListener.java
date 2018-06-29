package com.ewind.hl.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.view.EventSearchView;

import java.lang.ref.WeakReference;
import java.util.Calendar;

import static com.ewind.hl.ui.EventFormActivity.EVENT_AREA;
import static com.ewind.hl.ui.EventFormActivity.EVENT_DATE;
import static com.ewind.hl.ui.EventFormActivity.EVENT_ID;
import static com.ewind.hl.ui.EventFormActivity.EVENT_TYPE;

public class EventActionListener {

    public static final int UPDATE_REQUEST_CODE = 10;
    public static final int ADD_REQUEST_CODE = 11;

    private final WeakReference<Activity> activityWeakReference;

    public EventActionListener(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public void onUpdate(Event event) {
        Activity activity = activityWeakReference.get();

        Intent intent = new Intent(activity, EventFormActivity.class);
        intent.putExtra(EVENT_ID, event.getId());
        activity.startActivityForResult(intent, UPDATE_REQUEST_CODE);
    }

    public void onDelete(Event event) {
        Activity activity = activityWeakReference.get();
        new AlertDialog.Builder(activity)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Do you want to delete this event?")
                .setPositiveButton(android.R.string.ok, (d, v) -> doDelete(activity, event))
                .show();
    }

    private void doDelete(Activity activity, Event event) {
        new EventsDao(activity).delete(event);
        if (activity instanceof EventChangedListener) {
            ((EventChangedListener) activity).onEventDeleted(event);
        }
    }

    public void onAddNew() {
        Activity activity = activityWeakReference.get();
        EventSearchView eventSearchView = (EventSearchView) LayoutInflater.from(activity).inflate(R.layout.event_search, null);
        AlertDialog dialog = new AlertDialog.Builder(activity).setView(eventSearchView).create();
        eventSearchView.setOnEventClickListener(e -> {
            dialog.cancel();
            Intent intent = new Intent(activity, EventFormActivity.class);
            intent.putExtra(EVENT_TYPE, e.name());
            intent.putExtra(EVENT_DATE, getToday());
            activity.startActivityForResult(intent, ADD_REQUEST_CODE);
        });

        dialog.show();
    }

    private String getToday() {
        return EventDate.of(Calendar.getInstance()).toString();
    }

    public void onAddLike(Event event) {
        Activity activity = activityWeakReference.get();

        Intent intent = new Intent(activity, EventFormActivity.class);
        intent.putExtra(EVENT_DATE, getToday());
        intent.putExtra(EVENT_TYPE, event.getType().name());
        intent.putExtra(EVENT_AREA, event.getArea().getName());
        activity.startActivityForResult(intent, ADD_REQUEST_CODE);
    }
}
