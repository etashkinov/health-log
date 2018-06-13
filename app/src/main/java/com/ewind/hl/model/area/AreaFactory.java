package com.ewind.hl.model.area;

import com.ewind.hl.model.event.EventType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AreaFactory {

    private static final ObjectMapper MAPPER = new ObjectMapper(new YAMLFactory());

    public static class AreaConfig {
        private List<String> events;
        private Map<String, AreaConfig> parts;
        private String dual;
        private List<String> multiple;

        public AreaConfig() {
        }

        public List<String> getEvents() {
            return events;
        }

        public void setEvents(List<String> events) {
            this.events = events;
        }

        public Map<String, AreaConfig> getParts() {
            return parts;
        }

        public void setParts(Map<String, AreaConfig> parts) {
            this.parts = parts;
        }

        public String getDual() {
            return dual;
        }

        public void setDual(String dual) {
            this.dual = dual;
        }

        public List<String> getMultiple() {
            return multiple;
        }

        public void setMultiple(List<String> multiple) {
            this.multiple = multiple;
        }
    }


    public static Area getBody() {
        try(InputStream stream = AreaFactory.class.getClassLoader().getResourceAsStream("body.yml")) {
            AreaConfig config = MAPPER.readValue(stream, AreaConfig.class);
            return createArea("body", config);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to parse the body config", e);
        }
    }

    private static Area createArea(String name, AreaConfig config) {
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
            children.add(createArea("left_" + dual, config));
            children.add(createArea("right_" + dual, config));
        } else if (config.multiple != null) {
            List<String> multiple = config.multiple;
            config.multiple = null;
            for (String mult : multiple) {
                children.add(createArea(mult, config));
            }
        } else if (config.parts != null) {
            for (Map.Entry<String, AreaConfig> part : config.parts.entrySet()) {
                AreaConfig partConfig = part.getValue();

                if (partConfig == null) {
                    partConfig = new AreaConfig();
                    partConfig.parts = Collections.emptyMap();
                }

                if (partConfig.events == null) {
                    partConfig.events = new LinkedList<>();
                }

                partConfig.events.addAll(eventsToPropagateDown);
                children.add(createArea(part.getKey(), partConfig));
            }
        }

        return new Area(name, events, children);
    }

}