package com.ewind.hl.model.event.type;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ewind.hl.R;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.math.BigDecimal;
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
    private static final Class<? extends EventType> DEFAULT_MEASUREMENT_TYPE = MeasurementEventType.class;

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

    public static class EventValue {
        private String unit;
        private BigDecimal min;
        private BigDecimal max;
        private BigDecimal normalMin;
        private BigDecimal normalMax;
        private BigDecimal step;
        private BigDecimal normal;

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public BigDecimal getMin() {
            return min;
        }

        public BigDecimal getMax() {
            return max;
        }

        public void setRange(String range) {
            String[] rangeSplit = range.split(",");
            this.min = new BigDecimal(rangeSplit[0]);
            this.max = new BigDecimal(rangeSplit[1]);
        }

        public BigDecimal getNormalMin() {
            return normalMin;
        }

        public void setNormal(String normal) {
            String[] normalSplit = normal.split(",");
            this.normalMin = new BigDecimal(normalSplit[0]);
            this.normal = new BigDecimal(normalSplit[1]);
            this.normalMax = new BigDecimal(normalSplit[2]);
        }

        public BigDecimal getNormalMax() {
            return normalMax;
        }

        public BigDecimal getStep() {
            return step;
        }

        public void setStep(BigDecimal step) {
            this.step = step;
        }

        public BigDecimal getNormal() {
            return normal;
        }
    }

    public static class EventConfig {
        private Set<String> areas = new HashSet<>();
        private boolean propagateDown = false;
        private String type;
        private String accuracy;
        private String expiration;
        private EventValue value;

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

        public String getExpiration() {
            return expiration;
        }

        public void setExpiration(String expiration) {
            this.expiration = expiration;
        }

        public EventValue getValue() {
            return value;
        }

        public void setValue(EventValue value) {
            this.value = value;
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
            EventsConfig config = MAPPER.readValue(stream, EventsConfig.class);

            events = createEvents(config.symptoms, DEFAULT_SYMPTOM_TYPE);
            events.putAll(createEvents(config.measurements, DEFAULT_MEASUREMENT_TYPE));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse the body config", e);
        }
    }

    private static Map<String, EventType> createEvents(Map<String, EventConfig> configs, Class<? extends EventType> defaultType) {
        Map<String, EventType> result = new HashMap<>();
        for (Entry<String, EventConfig> entry : configs.entrySet()) {
            try {
                result.put(entry.getKey(), createEvent(entry.getKey(), entry.getValue(), defaultType));
            } catch (Exception e) {
                Log.e(TAG, "Failed to parse config for " + entry.getKey(), e);
            }
        }
        return result;
    }

    private static EventType createEvent(String name, EventConfig eventConfig, Class<? extends EventType> defaultType) throws Exception {
        if (eventConfig == null) {
            eventConfig = new EventConfig();
        }

        Class<? extends EventType> eventTypeClass = EVENT_TYPES.get(eventConfig.type);
        if (eventTypeClass == null) {
            eventTypeClass = defaultType;
        }

        return eventTypeClass.getConstructor(String.class, EventConfig.class).newInstance(name, eventConfig);
    }

    @NonNull
    public static <T extends EventType> T get(String typeName) {
        EventType result = events.get(typeName);
        if (result == null) {
            throw new IllegalArgumentException("Unknown typeName: " + typeName);
        }
        return (T) result;
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
