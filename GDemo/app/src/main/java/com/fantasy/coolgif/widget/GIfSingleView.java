package com.fantasy.coolgif.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.blur.BlurFactory;
import com.fantasy.coolgif.blur.FastBlur;
import com.fantasy.coolgif.main.MainApplication;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.utils.LogUtil;

/**
 * Created by fanlitao on 17/3/12.
 */

public class GIfSingleView extends LinearLayout {


    private ImageView mGifImageView;
    private TextView mGifTitle;
    private RequestManager imageLoader;

    public GIfSingleView(Context context) {
        super(context);
        init(context);
    }

    public GIfSingleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GIfSingleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        imageLoader = Glide.with(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mGifImageView = (ImageView) findViewById(R.id.img);
        mGifTitle = (TextView) findViewById(R.id.gif_title);
    }

    private GifItem gifItem;

    public void setGifItem(GifItem item) {
        this.gifItem = item;
        mGifTitle.setText(item.gif_title);
        imageLoader.load(gifItem.gif_url).asBitmap().
                diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if(resource != null) {
                    mGifImageView.setBackground(new BitmapDrawable(BlurFactory.getDefault().blur(resource)));
                }

            }
        });
    }

    public void startGifAnimation() {
        if (gifItem != null) {
            imageLoader.load(gifItem.gif_url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mGifImageView);
        }
    }

}
