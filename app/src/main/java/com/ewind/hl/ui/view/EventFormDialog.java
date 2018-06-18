package com.ewind.hl.ui.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;

public class EventFormDialog extends AlertDialog implements DialogInterface.OnClickListener {

    public interface EventDetailDialogListener {
        void onSubmit(EventDetail detail);
    }

    private final EventForm form;
    private final EventDetailDialogListener listener;

    public EventFormDialog(Context context, EventType type, EventDetail detail, EventDetailDialogListener listener) {
        super(context);
        this.listener = listener;

        Context themeContext = getContext();

        this.form = initForm(type, detail, themeContext);

        setButton(BUTTON_POSITIVE, themeContext.getString(android.R.string.ok), this);
        setButton(BUTTON_NEGATIVE, themeContext.getString(android.R.string.cancel), this);
    }

    private EventForm initForm(EventType type, EventDetail detail, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        EventForm result = (EventForm) inflater.inflate(R.layout.event_form, null);
        result.setEventDetail(type, detail);
        setView(result);
        return result;
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                listener.onSubmit(form.getEventDetail());
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
    }

}
