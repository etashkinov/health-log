package com.ewind.hl.model.event;

import android.support.annotation.NonNull;

import com.ewind.hl.model.event.detail.EventDetail;

import java.util.List;

public class EventTypeFactory {

    @NonNull
    public static <T extends EventDetail> EventType<T> get(String typeName) {
        return null; // TODO
    }

    @NonNull
    public static List<EventType> list() {
        return null; // TODO
    }
}
