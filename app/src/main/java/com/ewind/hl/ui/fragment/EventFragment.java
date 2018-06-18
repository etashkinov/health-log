package com.ewind.hl.ui.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.ewind.hl.ui.view.EventDetailForm;

public class EventFragment extends Fragment {

    private View detailForm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.event_form, null);
        view.findViewById(R.id.cancelButton).setOnClickListener(this::onCancel);
        view.findViewById(R.id.okButton).setOnClickListener(this::onOk);

        MainActivity.State state = ((MainActivity) getActivity()).getState();
        initHeader(view, state.getType());
        initDetailForm(view, state.getType(), getEvent(state));
        return view;
    }

    private Event getEvent(MainActivity.State state) {
        return EventsDao.getEvent(state.getType(), state.getArea(), state.getDate());
    }

    private void onOk(View view) {
        EventDetail detail = ((EventDetailForm) detailForm).getDetail();
        ((MainActivity) getActivity()).onEventUpdated(detail);
    }

    private void onCancel(View view) {
        ((MainActivity) getActivity()).onEventUpdated(null);
    }

    private void initHeader(View view, EventType type) {
        ImageView eventImage = view.findViewById(R.id.eventImage);
        eventImage.setImageDrawable(getEventDrawable(type));

        TextView eventText = view.findViewById(R.id.eventText);
        eventText.setText(type.name());
    }

    private Drawable getEventDrawable(EventType type) {
        return getContext().getDrawable(android.R.drawable.ic_menu_mylocation);
    }

    private void initDetailForm(View view, EventType type, Event event) {
        int layoutId = getEventFormLayoutId(type);
        detailForm = LayoutInflater.from(getContext()).inflate(layoutId, null);
        ((ViewGroup) view.findViewById(R.id.eventDetailContainer)).addView(detailForm);

        if (event != null) {
            ((EventDetailForm) detailForm).setDetail(event.getValue());
        }
    }

    private int getEventFormLayoutId(EventType type) {
        String name = "event_" + type.name().toLowerCase() + "_form";
        return getContext().getResources().getIdentifier(name, "layout", getContext().getPackageName());
    }
}
