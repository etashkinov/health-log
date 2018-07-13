package com.ewind.hl.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.LayoutInflater;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.history.HistoryActivity;
import com.ewind.hl.ui.model.EventModel;
import com.ewind.hl.ui.view.EventSearchView;

import org.joda.time.LocalDateTime;

import java.lang.ref.WeakReference;

import static com.ewind.hl.ui.EventFormActivity.EVENT;
import static com.ewind.hl.ui.EventFormActivity.EVENT_ID;

public class EventActionListener {

    public static final int UPDATE_REQUEST_CODE = 10;
    public static final int ADD_REQUEST_CODE = 11;

    private final WeakReference<Activity> activityWeakReference;

    public EventActionListener(Activity activity) {
        this.activityWeakReference = new WeakReference<>(activity);
    }

    public void onHistory(Event event) {
        Activity activity = activityWeakReference.get();

        Intent intent = new Intent(activity, HistoryActivity.class);
        intent.putExtra(HistoryActivity.EVENT_TYPE, event.getType().getName());
        intent.putExtra(HistoryActivity.EVENT_AREA, event.getArea());
        activity.startActivity(intent);
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
            Event<?> newEvent = e.create(LocalDateTime.now(), null, null);
            intent.putExtra(EVENT, EventModel.of(newEvent));
            activity.startActivityForResult(intent, ADD_REQUEST_CODE);
        });

        dialog.show();
    }

    public <T extends EventDetail> void onAddLike(Event<T> event) {
        Activity activity = activityWeakReference.get();

        Intent intent = new Intent(activity, EventFormActivity.class);
        Event<T> newEvent = event.getType().create(LocalDateTime.now(), event.getArea(), event.getDetail());
        intent.putExtra(EVENT, EventModel.of(newEvent));
        activity.startActivityForResult(intent, ADD_REQUEST_CODE);
    }
}
