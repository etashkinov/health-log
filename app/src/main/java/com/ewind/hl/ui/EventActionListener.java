package com.ewind.hl.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.KeyEvent;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.history.HistoryActivity;

import org.joda.time.LocalDateTime;

import java.lang.ref.WeakReference;

import static com.ewind.hl.ui.EventFormActivity.EVENT;
import static com.ewind.hl.ui.EventFormActivity.EVENT_ID;

public class EventActionListener {

    public static final int UPDATE_REQUEST_CODE = 10;
    public static final int ADD_REQUEST_CODE = 11;
    public static final int SEARCH_REQUEST_CODE = 12;

    private final WeakReference<Activity> activityWeakReference;

    public EventActionListener(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public void onHistory(Event event) {
        EventType type = event.getType();
        Area area = event.getArea();
        Activity activity = activityWeakReference.get();

        Intent intent = new Intent(activity, HistoryActivity.class);
        intent.putExtra(HistoryActivity.EVENT_TYPE, type.getName());
        intent.putExtra(HistoryActivity.EVENT_AREA, area);
        activity.startActivityForResult(intent, UPDATE_REQUEST_CODE);
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
                .setNegativeButton(android.R.string.cancel, (d, v) -> doDeleteCancelled(activity, event))
                .setOnKeyListener((d, k, e ) -> {
                    if (k == KeyEvent.KEYCODE_BACK) {
                        d.dismiss();
                        doDeleteCancelled(activity, event);
                    }
                    return true;
                }).show();
    }

    private void doDelete(Activity activity, Event event) {
        new EventsDao(activity).delete(event);
        if (activity instanceof EventChangedListener) {
            ((EventChangedListener) activity).onEventDeleted(event);
        }
    }

    private void doDeleteCancelled(Activity activity, Event event) {
        if (activity instanceof EventChangedListener) {
            ((EventChangedListener) activity).onEventUpdated(event);
        }
    }

    public void onAddNew(String eventType) {
        Activity activity = activityWeakReference.get();
        Intent intent = new Intent(activity, EventFormActivity.class);
        Event<?> newEvent = EventTypeFactory.get(eventType).create(LocalDateTime.now(), null, null);
        intent.putExtra(EVENT, newEvent);
        activity.startActivityForResult(intent, ADD_REQUEST_CODE);
    }

    public void onSelectEventType() {
        Activity activity = activityWeakReference.get();
        Intent intent = new Intent(activity, EventTypeSearchActivity.class);
        activity.startActivityForResult(intent, SEARCH_REQUEST_CODE);
    }

    public <T extends EventDetail> void onAddLike(Event<T> event) {
        Activity activity = activityWeakReference.get();

        Intent intent = new Intent(activity, EventFormActivity.class);
        Event newEvent = event.getType().create(LocalDateTime.now(), event.getArea(), event.getDetail());
        intent.putExtra(EVENT, newEvent);
        activity.startActivityForResult(intent, ADD_REQUEST_CODE);
    }
}
