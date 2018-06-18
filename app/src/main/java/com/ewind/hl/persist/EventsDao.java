package com.ewind.hl.persist;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.EventDetail;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventsDao {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static AppDatabase db;
    private EventEntityDao entityDao;

    public EventsDao(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context, AppDatabase.class, "health-log-db").build();
        }

        entityDao = db.eventEntityDao();
    }

    public List<Event> getEvents(Area area, EventDate date) {
        List<EventEntity> eventEntities = entityDao.findByAreaAndDate(area.getId(), date.toString());
        List<Event> result = new ArrayList<>(eventEntities.size());
        for (EventEntity eventEntity : eventEntities) {
            result.add(toEvent(eventEntity, area));
        }
        return result;
    }

    private Event toEvent(EventEntity eventEntity, Area area) {
        if (eventEntity == null) {
            return null;
        }

        try {
            EventType type = EventType.valueOf(eventEntity.getType());
            EventDetail detail = MAPPER.readValue(eventEntity.getValue(), type.getDetailClass());
            return new Event(detail, EventDate.of(eventEntity.getDate()), area, null);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse " + eventEntity, e);
        }
    }

    private EventEntity getEventEntity(EventType type, Area area, EventDate date) {
        List<EventEntity> result = entityDao.findByAreaAndDateAndType(area.getId(), date.toString(), type.name());
        if (result.isEmpty()) {
            return null;
        } else if (result.size() > 1) {
            throw new IllegalStateException("Not unique event per day: " + result);
        } else {
            return result.get(0);
        }
    }

    public Event getEvent(EventType type, Area area, EventDate date) {
        return toEvent(getEventEntity(type, area, date), area);
    }

    public void store(Event event) {
        EventEntity storedEvent = getEventEntity(event.getValue().getType(), event.getArea(), event.getDate());
        if (storedEvent == null) {
            EventEntity eventEntity = toEventEntity(0, event);
            entityDao.insert(eventEntity);
        } else {
            EventEntity eventEntity = toEventEntity(storedEvent.getId(), event);
            entityDao.update(eventEntity);
        }
    }

    private EventEntity toEventEntity(int id, Event event) {
        try {
            EventEntity result = new EventEntity();
            result.setId(id);
            result.setArea(event.getArea().getId());
            result.setDate(event.getDate().toString());
            result.setType(event.getValue().getType().name());
            result.setValue(MAPPER.writeValueAsString(event.getValue()));
            return result;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize " + event, e);
        }
    }
}
