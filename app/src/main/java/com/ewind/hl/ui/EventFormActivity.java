package com.ewind.hl.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.model.EventModel;
import com.ewind.hl.ui.view.EventDatePicker;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.detail.ValueDetailForm;

public class EventFormActivity extends AppCompatActivity {


    private static final String TAG = EventFormActivity.class.getName();


    public static final String EVENT_ID = "EVENT_ID";
    public static final String EVENT_TYPE = "EVENT_TYPE";
    public static final String EVENT_DATE = "EVENT_DATE";
    public static final String EVENT_AREA = "EVENT_AREA";
    public static final String EVENT_DETAIL = "EVENT_DETAIL";

    private long id;
    private EventModel model;
    private EventDatePicker eventDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_form);

        findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);
        findViewById(R.id.okButton).setOnClickListener(this::onOk);

        eventDatePicker = findViewById(R.id.eventDatePicker);
        eventDatePicker.setListener(d -> {});

        id = getIntent().getLongExtra(EVENT_ID, 0L);

        initModel();
    }

    private void initModel() {
        if (id != 0) {
            model = EventModel.of(new EventsDao(this).getEvent(id));
        } else {
            EventType type = EventType.valueOf(getIntent().getStringExtra(EVENT_TYPE));
            EventDate date = EventDate.of(getIntent().getStringExtra(EVENT_DATE));
            Area area = AreaFactory.getArea(getIntent().getStringExtra(EVENT_AREA));

            model = EventModel.empty(type, area, date);
        }

        setEvent(model);
    }

    private void setEvent(EventModel event) {
        initHeader(event.getType());

        eventDatePicker.setDate(event.getDate());

        initDetailForm(event);
    }

    private EventDetailForm detailForm;

    private void onOk(View view) {
        updateEvent();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EVENT_ID, id);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    private void updateEvent() {
        EventDetail detail = detailForm.getDetail();
        new EventsDao(this).store(new Event(
                id,
                eventDatePicker.getDate(),
                model.getType(),
                detail,
                model.getArea() == null ? AreaFactory.getBody() : model.getArea(),
                model.getNote()
        ));
    }

    private void onCancel(View view) {
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void initHeader(EventType type) {
        ImageView eventImage = findViewById(R.id.eventImage);
        eventImage.setImageDrawable(getEventDrawable(type));

        TextView eventText = findViewById(R.id.eventText);
        eventText.setText(LocalizationService.getEventTypeName(type));
    }

    private Drawable getEventDrawable(EventType type) {
        return getDrawable(android.R.drawable.ic_menu_mylocation);
    }

    private void initDetailForm(EventModel event) {
        detailForm = getDetailForm(event.getType());
        ((ViewGroup) findViewById(R.id.eventDetailContainer)).addView((View) detailForm);

        if (event.getDetail() != null) {
            detailForm.setDetail(event.getDetail());
        }
    }

    private EventDetailForm getDetailForm(EventType type) {
        try {
            String name = "event_" + type.name().toLowerCase() + "_form";
            int layout = getResources().getIdentifier(name, "layout", getPackageName());
            return (EventDetailForm) LayoutInflater.from(this).inflate(layout, null);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Detail form layout for " + type + " not found");
            ValueDetailForm valueDetailForm = (ValueDetailForm) LayoutInflater.from(this).inflate(R.layout.event_value_form, null);
            valueDetailForm.setEventType(type);
            return valueDetailForm;
        }
    }
}
