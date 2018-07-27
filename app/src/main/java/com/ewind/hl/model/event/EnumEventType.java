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
        this.step = 100 / enumType.getEnumConstants().length;
    }

    protected EnumEventType(String name, EventConfig config, Class<D> detailClass, Class<E> enumType) {
        super(name, detailClass, config);
        this.enumType = enumType;
        this.step = 100 / enumType.getEnumConstants().length;
    }

    public E getType(D detail) {
        return getType(detail.getValue());
    }

    @NonNull
    public BigDecimal getValue(int typeOrdinal) {
        return BigDecimal.valueOf(typeOrdinal * step);
    }

    @NonNull
    public E getType(BigDecimal value) {
        int index = value.intValue() / step;
        int length = enumType.getEnumConstants().length;
        index = index >= length ? length -1 : index;
        return enumType.getEnumConstants()[index];
    }

    @Override
    @NonNull
    public String getDescription(Event<D> event, Context context) {
        return getDescription(getType(event.getDetail()));
    }

    @NonNull
    public String getDescription(E type) {
        return LocalizationService.snakeCaseToReadable(type.name());
    }

    @NonNull
    public String getDescription(int value) {
        return getDescription(enumType.getEnumConstants()[value]);
    }
}
