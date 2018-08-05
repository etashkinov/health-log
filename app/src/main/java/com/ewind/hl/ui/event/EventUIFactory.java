package com.ewind.hl.ui.event;

import android.support.annotation.NonNull;

import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EnumEventType;
import com.ewind.hl.model.event.type.EventType;

import java.util.HashMap;
import java.util.Map;

public class EventUIFactory {
    private static final Map<Class<? extends EventType>, Class<? extends EventUI>> UI = new HashMap<>();

    static {
        UI.put(EnumEventType.class, EnumEventUI.class);
    }

    @NonNull
    public static <D extends EventDetail> EventUI<D> getUI(EventType<D> type) {
        try {
            Class<? extends EventType> typeClass = type.getClass();
            Class<? extends EventUI> uiClass = getUIClass(typeClass);
            if (uiClass == null) {
                uiClass = DefaultEventUI.class;
            }
            return (EventUI<D>) uiClass.getConstructor(EventType.class).newInstance(type);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot get ui for " + type, e);
        }
    }

    private static <D extends EventDetail> Class<? extends EventUI<D>> getUIClass(Class<? extends EventType> typeClass) {
        Class uiClass = UI.get(typeClass);
        if (uiClass == null) {
            Class<?> superclass = typeClass.getSuperclass();
            if (EventType.class.isAssignableFrom(superclass)) {
                return getUIClass((Class<? extends EventType>) superclass);
            } else {
                return null;
            }
        } else {
            return uiClass;
        }
    }
}
