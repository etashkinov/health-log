package com.ewind.hl.model.area;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AreaFactory {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());
    private static Area body;

    @NonNull
    public static List<Area> getAreas() {
        List<Area> result = new LinkedList<>();

        result.add(body);
        result.addAll(getParts(body));

        return result;
    }

    @NonNull
    private static List<Area> getParts(Area area) {
        List<Area> result = new LinkedList<>();
        List<Area> parts = area.getParts();
        if (parts != null) {
            for (Area part : parts) {
                result.add(part);
                result.addAll(getParts(part));
            }
        }

        return result;
    }

    public static class AreaConfig {
        private List<String> events = new LinkedList<>();
        private Map<String, AreaConfig> parts = Collections.emptyMap();
        private String dual;
        private List<String> multiple = Collections.emptyList();

        public void setEvents(List<String> events) {
            this.events = events;
        }

        public void setParts(Map<String, AreaConfig> parts) {
            this.parts = parts;
        }

        public void setDual(String dual) {
            this.dual = dual;
        }

        public void setMultiple(List<String> multiple) {
            this.multiple = multiple;
        }
    }

    public static Area getArea(String name) {
        return getArea(body, name);
    }

    public static Area getArea(Area area, String name) {
        if (name == null) {
            return null;
        } else if (area.getName().equals(name)) {
            return area;
        } else {
            for (Area part : area.getParts()) {
                Area result = getArea(part, name);
                if (result != null) {
                    return result;
                }
            }

            return null;
        }
    }

    public static Area getBody() {
        return body;
    }

    public static void initBody(Context context) {
        try (InputStream stream = context.getResources().openRawResource(R.raw.body)) {
            initBody(stream);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load body configuration", e);
        }
    }

    static void initBody(InputStream stream) {
        if (body == null) try {
            AreaConfig config = MAPPER.readValue(stream, AreaConfig.class);
            body = createArea(null, "body", config);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse the body config", e);
        }
    }

    private static Area createArea(String scope, String name, AreaConfig config) {
        List<EventType> events = new ArrayList<>(config.events.size());
        List<String> eventsToPropagateDown = new LinkedList<>();

        for (String eventName : config.events) {
            EventType event = EventType.valueOf(eventName);
            events.add(event);
            if (event.isPropagateDown()) {
                eventsToPropagateDown.add(eventName);
            }
        }

        List<Area> children = new LinkedList<>();
        if (config.dual != null) {
            String dual = config.dual;
            config.dual = null;
            children.add(createArea(addScope(scope,"left"), dual, config));
            children.add(createArea(addScope(scope,"right"), dual, config));
        } else if (!config.multiple.isEmpty()) {
            List<String> multiple = config.multiple;
            config.multiple = Collections.emptyList();
            for (String mult : multiple) {
                children.add(createArea(addScope(scope,mult), "", config));
            }
        } else if (!config.parts.isEmpty()) {
            for (Entry<String, AreaConfig> part : config.parts.entrySet()) {
                AreaConfig partConfig = part.getValue();

                if (partConfig == null) {
                    partConfig = new AreaConfig();
                }

                partConfig.events.addAll(eventsToPropagateDown);
                children.add(createArea(scope, part.getKey(), partConfig));
            }
        }

        return new Area(addScope(scope, name), events, children);
    }

    private static String addScope(String scope, String value) {
        if (TextUtils.isEmpty(scope)) {
            return value;
        } else {
            return scope + "_" + value;
        }
    }

}
