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
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.model.EventModel;
import com.ewind.hl.ui.view.EventDatePicker;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.area.AreaSelector;

public class EventFormActivity extends AppCompatActivity implements EventChangedListener {
    private static final String TAG = EventFormActivity.class.getName();

    public static final String EVENT_ID = "EVENT_ID";
    public static final String EVENT = "EVENT";

    private long id;
    private EventModel model;
    private EventDatePicker eventDatePicker;
    private AreaSelector areaSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);
        findViewById(R.id.okButton).setOnClickListener(this::onOk);

        eventDatePicker = findViewById(R.id.eventDatePicker);
        eventDatePicker.setListener(d -> {});

        areaSelector = findViewById(R.id.areaSelector);

        id = getIntent().getLongExtra(EVENT_ID, 0L);

        initModel();
    }

    private void initModel() {
        if (id != 0) {
            model = EventModel.of(new EventsDao(this).getEvent(id));
        } else {
            model = (EventModel) getIntent().getSerializableExtra(EVENT);
            findViewById(R.id.deleteButton).setVisibility(View.INVISIBLE);
        }

        setEvent(model);
    }

    private void setEvent(EventModel event) {
        initHeader(event.getType());

        eventDatePicker.setDate(event.getDate());
        areaSelector.setArea(event.getType(), event.getArea());

        initDetailForm(event);
    }

    private EventDetailForm detailForm;

    private void onOk(View view) {
        updateEvent();
        finishOk();
    }

    private void finishOk() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EVENT_ID, id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    public void onDelete(View view) {
        new EventActionListener(this).onDelete(model.toEvent(id));
    }

    private void updateEvent() {
        EventDetail detail = detailForm.getDetail();
        new EventsDao(this).store(new Event(
                id,
                eventDatePicker.getDate(),
                model.getType(),
                detail,
                areaSelector.getArea() == null ? AreaFactory.getBody() : areaSelector.getArea(),
                model.getNote()
        ));
    }

    private void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void initHeader(EventType type) {
        ImageView eventImage = findViewById(R.id.eventImage);
        Drawable drawable = EventUI.getEventDrawable(type, this);
        eventImage.setImageDrawable(drawable);

        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(LocalizationService.getEventTypeName(type));
    }

    private void initDetailForm(EventModel event) {
        detailForm = EventUI.getEventDetailForm(event, this);
        ((ViewGroup) findViewById(R.id.eventDetailContainer)).addView((View) detailForm);

        if (event.getDetail() != null) {
            detailForm.setDetail(event.getDetail());
        }
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
        finishOk();
    }
}
