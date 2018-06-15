package com.ewind.hl;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.view.EventFormDialog;

public class EventFormFragment extends DialogFragment {

    private EventType type;
    private EventDetail detail;
    private EventFormDialog.EventDetailDialogListener listener;

    public void setEvent(EventType type, EventDetail detail, EventFormDialog.EventDetailDialogListener listener) {
        this.type = type;
        this.detail = detail;
        this.listener = listener;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new EventFormDialog(getContext(), type, detail, listener);
    }
}
