package com.ewind.hl.model.event;

import android.content.Context;

import com.ewind.hl.model.event.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.LocalizationService;

public abstract class EnumEventType<E extends Enum<E>, T extends ValueDetail> extends SymptomEventType<T> {

    private final Class<E> enumType;
    private final int step;

    protected EnumEventType(String name, EventConfig config, Class<E> enumType) {
        super(name, config);
        this.enumType = enumType;
        this.step = 100 / enumType.getEnumConstants().length;
    }


    protected EnumEventType(String name, EventConfig config, Class<T> detailClass, Class<E> enumType) {
        super(name, detailClass, config);
        this.enumType = enumType;
        this.step = 100 / enumType.getEnumConstants().length;
    }

    public E getType(ValueDetail detail) {
        return getType(detail.getValue().intValue());
    }

    public E getType(int value) {
        int index = value / step;
        int length = enumType.getEnumConstants().length;
        index = index >= length ? length -1 : index;
        return enumType.getEnumConstants()[index];
    }

    @Override
    public String getDescription(Event<T> event, Context context) {
        return LocalizationService.snakeCaseToReadable(getType(event.getDetail()).name());
    }
}
