package com.movies.ui;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * Created by michaelpaschenko on 9/8/17.
 * Helper adapters for data binding
 */

public class BindingAdapters {

    /**
     * Adapter to join strings
     * @param view
     * @param show
     */
    @BindingAdapter("join")
    public static void showHide(TextView view, List<String> show) {
        view.setText(TextUtils.join(", ", show));
    }

    /**
     * Adapter to load image into imageview by url provided
     * @param view
     * @param imageUrl
     */
    @BindingAdapter({"bind:url"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(imageUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(view);
    }
}
