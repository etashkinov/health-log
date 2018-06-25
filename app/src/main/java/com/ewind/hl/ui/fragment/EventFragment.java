package com.ewind.hl.ui.fragment;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewind.hl.MainActivity;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.persist.EventsDao;
import com.ewind.hl.ui.LocalizationService;
import com.ewind.hl.ui.view.EventDetailForm;
import com.ewind.hl.ui.view.detail.ValueDetailForm;

public class EventFragment extends Fragment {
    private static final String TAG = EventFragment.class.getName();

    private EventDetailForm detailForm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_form, null);
        view.findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);
        view.findViewById(R.id.okButton).setOnClickListener(this::onOk);
        view.findViewById(R.id.eventHistory).setOnClickListener(this::onShowHistory);

        MainActivity.State state = ((MainActivity) getActivity()).getState();
        initHeader(view, state.getType());
        initDetailForm(view, state);
        return view;
    }

    private void onShowHistory(View view) {
        ((MainActivity) getActivity()).onModeChanged(MainActivity.ViewMode.HISTORY);
    }

    private void onOk(View view) {
        EventDetail detail = detailForm.getDetail();
        ((MainActivity) getActivity()).onEventUpdated(detail);
    }

    private void onCancel(View view) {
        ((MainActivity) getActivity()).onEventUpdated(null);
    }

    private void initHeader(View view, EventType type) {
        ImageView eventImage = view.findViewById(R.id.eventImage);
        eventImage.setImageDrawable(getEventDrawable(type));

        TextView eventText = view.findViewById(R.id.eventText);
        eventText.setText(LocalizationService.getEventTypeName(type));
    }

    private Drawable getEventDrawable(EventType type) {
        return getContext().getDrawable(android.R.drawable.ic_menu_mylocation);
    }

    private void initDetailForm(View view, MainActivity.State state) {
        detailForm = getDetailForm(state.getType());
        ((ViewGroup) view.findViewById(R.id.eventDetailContainer)).addView((View) detailForm);

        Event event = new EventsDao(getContext()).getEvent(state.getType(), state.getArea(), state.getDate());

        if (event != null) {
            detailForm.setDetail(event.getValue());
        }
    }

    private EventDetailForm getDetailForm(EventType type) {
        try {
            String name = "event_" + type.name().toLowerCase() + "_form";
            int layout = getContext().getResources().getIdentifier(name, "layout", getContext().getPackageName());
            return (EventDetailForm) LayoutInflater.from(getContext()).inflate(layout, null);
        } catch (Resources.NotFoundException e) {
            Log.w(TAG, "Detail form layout for " + type + " not found");
            ValueDetailForm valueDetailForm = (ValueDetailForm) LayoutInflater.from(getContext()).inflate(R.layout.event_value_form, null);
            valueDetailForm.setEventType(type);
            return valueDetailForm;
        }
    }
}
