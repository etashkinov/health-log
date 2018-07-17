package com.ewind.hl.model.event;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ewind.hl.R;
import com.ewind.hl.model.event.detail.EventDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class EventTypeFactory {

    private static final String TAG = EventTypeFactory.class.getName();
    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    private static final Map<String, Class<? extends EventType>> EVENT_TYPES = new HashMap<>();
    private static final Class<? extends EventType> DEFAULT_SYMPTOM_TYPE = SeverityEventType.class;

    public static void registerType(String name, Class<? extends EventType> type) {
        EVENT_TYPES.put(name, type);
    }

    static {
        registerType("pain", PainEventType.class);
        registerType("amount", AmountEventType.class);
        registerType("size", SizeEventType.class);
        registerType("severity", SeverityEventType.class);
    }

    private static Map<String, EventType> events;

    public static class EventsConfig {
        private Map<String, EventConfig> symptoms = new HashMap<>();
        private Map<String, EventConfig> measurements = new HashMap<>();
        private Map<String, EventConfig> diagnosis = new HashMap<>();

        public void setSymptoms(Map<String, EventConfig> symptoms) {
            this.symptoms = symptoms;
        }

        public void setDiagnosis(Map<String, EventConfig> diagnosis) {
            this.diagnosis = diagnosis;
        }

        public void setMeasurements(Map<String, EventConfig> measurements) {
            this.measurements = measurements;
        }
    }

    public static class EventConfig {
        private Set<String> areas = new HashSet<>();
        private boolean propagateDown = false;
        private String type;
        private String accuracy;

        public void setAreas(Set<String> areas) {
            this.areas = areas;
        }

        public void setPropagateDown(boolean propagateDown) {
            this.propagateDown = propagateDown;
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }

        public Set<String> getAreas() {
            return areas;
        }

        public boolean isPropagateDown() {
            return propagateDown;
        }

        public String getType() {
            return type;
        }

        public String getAccuracy() {
            return accuracy;
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
            try {
                result.put(entry.getKey(), createEvent(entry.getKey(), entry.getValue()));
            } catch (Exception e) {
                Log.e(TAG, "Failed to parse config for " + entry.getKey());
            }
        }
        return result;
    }

    private static EventType createEvent(String name, EventConfig eventConfig) throws Exception {
        if (eventConfig == null) {
            eventConfig = new EventConfig();
        }

        Class<? extends EventType> eventTypeClass = EVENT_TYPES.get(eventConfig.type);
        if (eventTypeClass == null) {
            eventTypeClass = DEFAULT_SYMPTOM_TYPE;
        }

        return eventTypeClass.getConstructor(String.class, EventConfig.class).newInstance(name, eventConfig);
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
    public static Set<String> getAreas(EventType<?> type) {
        return type.getAreas();
    }

}
