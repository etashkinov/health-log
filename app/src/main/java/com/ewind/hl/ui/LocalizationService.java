package com.ewind.hl.ui;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.EventType;

public class LocalizationService {

    public static String getAreaName(Area area) {
        String title = area.getName().replaceAll("_", " ");
        return Character.toUpperCase(title.charAt(0)) + title.substring(1);
    }

    public static String getEventTypeName(EventType type) {
        String title = type.name().toLowerCase().replaceAll("_", " ");
        return Character.toUpperCase(title.charAt(0)) + title.substring(1);
    }

}
