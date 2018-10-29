package com.ewind.hl.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.ewind.hl.R;

public class UiHelper {

    public static void setTint(ImageView image, int color) {
        image.setImageTintList(ColorStateList.valueOf(color));
        image.setImageTintMode(PorterDuff.Mode.SRC_ATOP);
    }

    public static void setPersonPhoto(ImageView imageView, Uri photoUri, Context context) {
        RequestManager imageRequest = Glide.with(context);
        RequestBuilder<Drawable> builder = photoUri == null ? imageRequest.load(R.drawable.ic_user) : imageRequest.load(photoUri);
        builder.apply(RequestOptions.circleCropTransform()).into(imageView);
    }

}
