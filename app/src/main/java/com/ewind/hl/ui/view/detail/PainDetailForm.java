package com.ewind.hl.ui.view.detail;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.EventType;
import com.ewind.hl.model.event.detail.PainDetail;

import java.util.ArrayList;
import java.util.List;

public class PainDetailForm extends ValueDetailForm<PainDetail> {

    private MultiAutoCompleteTextView painType;

    public PainDetailForm(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEventType(EventType.PAIN);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        painType = findViewById(R.id.painTypeInput);
        ArrayAdapter<PainDetail.PainType> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, PainDetail.PainType.values());
        painType.setAdapter(adapter);
        painType.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    @Override
    public void setDetail(PainDetail detail) {
        super.setDetail(detail);
        painType.setText(TextUtils.join(", ", detail.getPainTypes()));
    }

    @Override
    public PainDetail getDetail() {
        String[] typeStrings = painType.getText().toString().split(",");
        List<PainDetail.PainType> painTypes = new ArrayList<>(typeStrings.length);
        for (String typeString: typeStrings) {
            try {
                painTypes.add(PainDetail.PainType.valueOf(typeString.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                Log.e(PainDetailForm.class.getName(), "Failed to parse pain type '" + typeString + "'", e);
            }
        }
        return new PainDetail(getValue(), painTypes);
    }
}
