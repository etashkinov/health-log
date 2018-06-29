package com.ewind.hl.ui.view;

import com.ewind.hl.model.event.detail.EventDetail;

public interface EventDetailView<T extends EventDetail> {
    void setDetail(T detail);
    T getDetail();
}
