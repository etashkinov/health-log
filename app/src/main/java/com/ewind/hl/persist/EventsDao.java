package com.ewind.hl.persist;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.EventTypeFactory;
import com.ewind.hl.model.event.Score;
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
            db = Room.databaseBuilder(context, AppDatabase.class, "health-log-db")
                    .allowMainThreadQueries()
                    .build();
        }

        entityDao = db.eventEntityDao();
    }

    public List<Event> getEvents(Area area, EventDate date) {
        List<EventEntity> eventEntities = entityDao.findByAreaAndDate(area.getName(), date.toString());
        return toEvents(eventEntities);
    }

    @NonNull
    private List<Event> toEvents(List<EventEntity> eventEntities) {
        List<Event> result = new ArrayList<>(eventEntities.size());
        for (EventEntity eventEntity : eventEntities) {
            result.add(toEvent(eventEntity));
        }
        return result;
    }

    private <T extends EventDetail> Event<T> toEvent(EventEntity eventEntity) {
        if (eventEntity == null) {
            return null;
        }

        try {
            EventType<T> type = EventTypeFactory.get(eventEntity.getType());
            T detail = MAPPER.readValue(eventEntity.getValue(), type.getDetailClass());
            Area area = AreaFactory.getArea(eventEntity.getArea());
            EventDate date = EventDateConverter.deserialize(eventEntity.getDate());
            Score score = new Score(eventEntity.getScore());
            return new Event<>(eventEntity.getId(), date, type, detail, area, eventEntity.getNote(), score);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to parse " + eventEntity, e);
        }
    }


    public Event getEvent(long id) {
        return toEvent(entityDao.findById(id));
    }

    public void store(Event<?> event) {
        if (event.getId() == 0) {
            EventEntity eventEntity = toEventEntity(event);
            entityDao.insert(eventEntity);
        } else {
            EventEntity eventEntity = toEventEntity(event);
            entityDao.update(eventEntity);
        }
    }

    private EventEntity toEventEntity(Event event) {
        try {
            EventEntity result = new EventEntity();
            result.setId(event.getId());
            result.setArea(event.getArea().getName());
            result.setDate(EventDateConverter.serialize(event.getDate()));
            result.setType(event.getType().getName());
            result.setScore(event.getScore().getValue());
            result.setNote(event.getNote());
            result.setValue(MAPPER.writeValueAsString(event.getDetail()));
            return result;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize " + event, e);
        }
    }

    public List<Event> getEvents(String name, Area area, EventDate from, EventDate till) {
        String areaName = area.getName();
        String fromDate = EventDateConverter.serialize(from);
        String tillDate = EventDateConverter.serialize(till);
        List<EventEntity> eventEntities = entityDao.findByAreaAndDateRangeAndType(areaName, fromDate, tillDate, name);
        return toEvents(eventEntities);
    }

    public void delete(Event event) {
        entityDao.delete(toEventEntity(event));
    }

    public List<Event> getLatestEvents() {
        return toEvents(entityDao.findLatest());
    }

    public Event getLatestEvent(EventType type) {
        return toEvent(entityDao.findLatest(type.getName()));
    }
}
