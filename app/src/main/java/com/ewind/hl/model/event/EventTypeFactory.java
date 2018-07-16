package com.ewind.hl.model.event;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ewind.hl.R;
import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.detail.PainDetail;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.joda.time.Period;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EventTypeFactory {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private static final Map<String, Class<? extends EventDetail>> EVENT_DETAILS;
    public static final Class<ValueDetail> DEFAULT_DETAIL = ValueDetail.class;

    static {
        EVENT_DETAILS = new HashMap<>();
        EVENT_DETAILS.put("pain", PainDetail.class);
    }

    private static Map<String, EventType> events;

    public static class EventsConfig {
        private Map<String, EventConfig> symptoms = new HashMap<>();
        private Map<String, EventConfig> diagnosis = new HashMap<>();

        public void setSymptoms(Map<String, EventConfig> symptoms) {
            this.symptoms = symptoms;
        }

        public void setDiagnosis(Map<String, EventConfig> diagnosis) {
            this.diagnosis = diagnosis;
        }
    }

    public static class EventConfig {
        private Set<String> areas = new HashSet<>();
        private boolean propagateDown = false;
        private String detail;
        private String accuracy;

        public void setAreas(Set<String> areas) {
            this.areas = areas;
        }

        public void setPropagateDown(boolean propagateDown) {
            this.propagateDown = propagateDown;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }
    }

    public static void initEvents(Context context) {
        try (InputStream stream = context.getResources().openRawResource(R.raw.events)) {
            initEvents(stream);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load events configuration", e);
        }
    }

    static void initEvents(InputStream stream) {
        if (events == null) try {
            Map<String, EventConfig> symptoms = MAPPER.readValue(stream, EventsConfig.class).symptoms;
            events = createEvents(symptoms);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse the body config", e);
        }
    }

    private static Map<String, EventType> createEvents(Map<String, EventConfig> symptoms) {
        Map<String, EventType> result = new HashMap<>();
        for (Entry<String, EventConfig> entry : symptoms.entrySet()) {
            result.put(entry.getKey(), createEvent(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    private static EventType createEvent(String name, EventConfig eventConfig) {
        if (eventConfig == null) {
            eventConfig = new EventConfig();
        }

        Accuracy accuracy = getAccuracy(eventConfig);

        Set<String> areas = getAreas(eventConfig);

        Class<? extends EventDetail> detailClass = EVENT_DETAILS.get(name);
        if (detailClass == null) {
            detailClass = DEFAULT_DETAIL;
        }

        return new SymptomEventType(name, Period.hours(accuracy.toHours()),
                accuracy, areas, detailClass, eventConfig.propagateDown);
    }

    @NonNull
    private static Accuracy getAccuracy(EventConfig eventConfig) {
        String accuracyStr = eventConfig.accuracy;
        Accuracy accuracy;
        if (accuracyStr == null) {
            accuracy = Accuracy.QUARTER;
        } else {
            accuracy = Accuracy.valueOf(accuracyStr.toUpperCase());
        }
        return accuracy;
    }

    private static Set<String> getAreas(EventConfig eventConfig) {
        Set<String> areas;
        if (eventConfig.propagateDown) {
            areas = new HashSet<>();
            for (String configArea : eventConfig.areas) {
                addArea(areas, AreaFactory.getArea(configArea));
            }
        } else {
            areas = eventConfig.areas;
        }
        return areas;
    }

    private static void addArea(Set<String> areas, Area area) {
        areas.add(area.getName());
        for (Area part : area.getParts()) {
            addArea(areas, part);
        }
    }

    @NonNull
    public static <T extends EventDetail> EventType<T> get(String typeName) {
        return events.get(typeName);
    }

    @NonNull
    public static Collection<EventType> getAll() {
        return events.values();
    }


    @NonNull
    public static Set<String> getAreas(EventType type) {
        return type.getAreas();
    }

}
