package com.ewind.hl.persist;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.ewind.hl.model.area.Area;
import com.ewind.hl.model.area.AreaFactory;
import com.ewind.hl.model.event.Event;
import com.ewind.hl.model.event.EventDate;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.detail.EventDetail;
import com.ewind.hl.model.event.type.EventType;
import com.ewind.hl.model.event.type.EventTypeFactory;
import com.ewind.hl.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventsDao {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final EventEntityDao entityDao;
    private final PersonService personService;

    public EventsDao(Context context) {
        this.personService = new PersonService(context);
        this.entityDao = AppDatabase.getInstance(context).eventEntityDao();
    }

    public <D extends EventDetail> List<Event<D>> getEvents(EventType<D> type, Area area) {
        List<EventEntity> eventEntities = entityDao.findByAreaAndType(area.getName(), type.getName(), getOwner());
        return (List) toEvents(eventEntities);
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


    public <D extends EventDetail> Event<D> getEvent(long id) {
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
            result.setOwner(getOwner());
            result.setReporter(personService.getCurrentAccountEmail());
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
        List<EventEntity> eventEntities = entityDao.findByAreaAndDateRangeAndType(areaName, fromDate, tillDate, name, getOwner());
        return toEvents(eventEntities);
    }

    public void delete(Event event) {
        entityDao.delete(toEventEntity(event));
    }

    public List<Event> getLatestEvents() {
        return toEvents(entityDao.findLatest(getOwner()));
    }

    public void getLatestEvents(Observer<List<Event>> observer) {
        AsyncTask.execute(() -> {
            List<Event> events = getLatestEvents();
            observer.onChanged(events);
        });
    }

    public Event getLatestEvent(EventType type) {
        String current = getOwner();
        return toEvent(entityDao.findLatest(type.getName(), current));
    }

    private String getOwner() {
        return personService.getCurrentId();
    }

    public List<Event> getAll() {
        return toEvents(entityDao.findAll());
    }

    public void refreshEventsWithEmptyOwner() {
        entityDao.updateEmptyOwner(getOwner());
    }

    public void refreshEventsWithEmptyReporter() {
        String currentAccountEmail = personService.getCurrentAccountEmail();
        entityDao.updateEmptyReporter(currentAccountEmail);
    }
}
