package com.ewind.hl.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.ewind.hl.R;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.ScoreBand;

public class TintHelper {
    public static void setScoreTint(ImageView image, Score score) {
        Context context = image.getContext();
        int eventTint = getScoreTint(score, context);
        if (eventTint != 0) {
            int color = ContextCompat.getColor(context, eventTint);
            image.setImageTintList(ColorStateList.valueOf(color));
            image.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
        }
    }

    public static int getScoreTint(Score score, Context context) {
        int band = new ScoreBand(score).getBand();
        int result = context.getResources().getIdentifier("severity" + band, "color", context.getPackageName());
        return result == 0 ? R.color.colorAccent : result;
    }

}
