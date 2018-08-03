package com.ewind.hl.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.event.EventFormView;
import com.ewind.hl.ui.event.EventUIFactory;

public class EventFormActivity<D extends EventDetail> extends AppCompatActivity implements EventChangedListener {
    private static final String TAG = EventFormActivity.class.getName();

    public static final String EVENT_ID = "EVENT_ID";
    public static final String EVENT = "EVENT";

    private EventFormView<D> eventFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);
        findViewById(R.id.okButton).setOnClickListener(this::onOk);

        ViewGroup eventFormContainer = findViewById(R.id.eventFormContainer);

        Event<D> event = getEventFromIntent(getIntent());
        EventType<D> type = event.getType();
        initHeader(type);

        eventFormView = EventUIFactory.getUI(type).createForm(eventFormContainer);
        eventFormView.setEvent(event);

        eventFormContainer.addView((View) eventFormView);
    }


    private Event<D> getEventFromIntent(Intent intent) {
        long id = intent.getLongExtra(EVENT_ID, 0L);
        if (id != 0) {
            return new EventsDao(this).getEvent(id);
        } else {
            return (Event<D>) intent.getSerializableExtra(EVENT);
        }
    }


    private void onOk(View view) {
        Event event = updateEvent();
        finishOk(event.getId());
    }

    private void finishOk(long id) {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);
        resultIntent.putExtra(EVENT_ID, id);
        finish();
    }

    private Event updateEvent() {
        Event event = eventFormView.getEvent();
        new EventsDao(this).store(event);
        return event;
    }

    private void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void initHeader(EventType<D> type) {
        ImageView eventImage = findViewById(R.id.eventImage);
        Drawable drawable = EventUI.getEventTypeDrawable(type, this);
        eventImage.setImageDrawable(drawable);

        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(LocalizationService.getEventTypeName(this, type));
    }

    @Override
    public void onEventCreated(Event event) {
        // do nothing
    }

    @Override
    public void onEventUpdated(Event event) {
        // do nothing
    }

    @Override
    public void onEventDeleted(Event event) {
        finishOk(event.getId());
    }
}
