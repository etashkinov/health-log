package com.ewind.hl.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.ewind.hl.R;
import com.ewind.hl.model.event.Score;
import com.ewind.hl.model.event.ScoreBand;

public class UiHelper {
    public static void setScoreTint(ImageView image, Score score) {
        Context context = image.getContext();
        int eventTint = getScoreTint(score, context);
        if (eventTint != 0) {
            int color = ContextCompat.getColor(context, eventTint);
            setTint(image, color);
        }
    }

    public static void setTint(ImageView image, int color) {
        image.setImageTintList(ColorStateList.valueOf(color));
        image.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
    }

    public static int getScoreTint(Score score, Context context) {
        int band = new ScoreBand(score).getBand();
        int result = context.getResources().getIdentifier("severity" + band, "color", context.getPackageName());
        return result == 0 ? R.color.colorAccent : result;
    }

    public static void setPersonPhoto(ImageView imageView, Uri photoUri, Context context) {
        RequestManager imageRequest = Glide.with(context);
        RequestBuilder<Drawable> builder = photoUri == null ? imageRequest.load(R.drawable.ic_user) : imageRequest.load(photoUri);
        builder.apply(RequestOptions.circleCropTransform()).into(imageView);
    }

}
