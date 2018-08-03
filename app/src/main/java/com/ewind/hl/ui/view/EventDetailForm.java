package com.ewind.hl.ui.view;

import com.ewind.hl.model.event.detail.EventDetail;

public interface EventDetailForm<D extends EventDetail> {
    void setDetail(D detail);
    D getDetail();
}
