package com.ewind.hl.model.event.type;

import android.content.Context;

import com.ewind.hl.model.event.ScoreBand;
import com.ewind.hl.model.event.type.EventTypeFactory.EventConfig;
import com.ewind.hl.model.event.detail.ValueDetail;
import com.ewind.hl.ui.LocalizationService;

public abstract class EnumEventType<E extends Enum<E>, D extends ValueDetail> extends ScoreEventType<D> {

    private final Class<E> enumType;

    protected EnumEventType(String name, EventConfig config, Class<E> enumType) {
        super(name, config);
        this.enumType = enumType;
    }

    protected EnumEventType(String name, EventConfig config, Class<D> detailClass, Class<E> enumType) {
        super(name, detailClass, config);
        this.enumType = enumType;
    }

    @Override
    public String getDescription(int score, Context context) {
        E type = new ScoreBand(score).getType(enumType);
        return LocalizationService.snakeCaseToReadable(type.name());
    }
}
