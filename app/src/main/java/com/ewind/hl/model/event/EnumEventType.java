package com.ewind.hl.model.event;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.LocalizationService;

import java.math.BigDecimal;

public abstract class EnumEventType<E extends Enum<E>, D extends ValueDetail> extends SymptomEventType<D> {

    private final Class<E> enumType;
    private final int step;

    protected EnumEventType(String name, EventConfig config, Class<E> enumType) {
        super(name, config);
        this.enumType = enumType;
        this.step = getMaximum() / (enumType.getEnumConstants().length - 1);
    }

    protected EnumEventType(String name, EventConfig config, Class<D> detailClass, Class<E> enumType) {
        super(name, detailClass, config);
        this.enumType = enumType;
        this.step = getMaximum() / (enumType.getEnumConstants().length - 1);
    }

    public E getType(D detail) {
        return getType(detail.getValue());
    }

    public E getType(BigDecimal value) {
        return getType(value.intValue() / step);
    }

    public E getType(int ordinal) {
        return enumType.getEnumConstants()[ordinal];
    }

    @Override
    public D createDetail(int ordinal) {
        return createDetail(ordinal * step);
    }

    @Override
    @NonNull
    public String getDescription(Event<D> event, Context context) {
        return getDescription(event.getDetail(), context);
    }

    public String getDescription(D detail, Context context) {
        return detail == null ? "" : getDescription(getType(detail), context);
    }

    @NonNull
    public String getDescription(E type, Context context) {
        return LocalizationService.snakeCaseToReadable(type.name());
    }

    @NonNull
    public String getDescription(int value, Context context) {
        return getDescription(enumType.getEnumConstants()[value], context);
    }
}
