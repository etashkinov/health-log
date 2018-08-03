package com.ewind.hl.ui.event;

import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.ui.event.def.DefaultEventUI;

import java.util.HashMap;
import java.util.Map;

public class EventUIFactory {

    private static final Map<String, EventUI<?>> UI = new HashMap<>();
    private static final DefaultEventUI DEFAULT_EVENT_UI = new DefaultEventUI();

    public static <D extends EventDetail> EventUI<D> getUI(EventType<D> type) {
        EventUI<D> ui = (EventUI<D>) UI.get(type.getName());
        if (ui == null) {
            return DEFAULT_EVENT_UI;
        } else {
            return ui;
        }
    }
}
