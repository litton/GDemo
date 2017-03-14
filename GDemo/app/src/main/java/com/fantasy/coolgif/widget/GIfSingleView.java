package com.fantasy.coolgif.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.blur.BlurFactory;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.utils.LogUtil;

/**
 * Created by fanlitao on 17/3/12.
 */

public class GIfSingleView extends LinearLayout {


    private ImageView mGifImageView;
    private TextView mGifTitle;
    private RequestManager imageLoader;

    private boolean mStartedPlayGIfAnimation = false;

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
        if (mGifImageView == null) {
            mGifImageView = (ImageView) findViewById(R.id.img);
        }
        if (mGifTitle == null) {
            mGifTitle = (TextView) findViewById(R.id.gif_title);
        }
    }

    private GifItem gifItem;
    private SimpleTarget<Bitmap> simpleTarget;

    public void setGifImageView(ImageView imageView) {
        mGifImageView = imageView;
    }

    public void setGifTitle(TextView title) {
        mGifTitle = title;
    }


    public void clearGlide() {
        if (gifItem != null) {
            if (simpleTarget != null) {
                Glide.clear(simpleTarget);
            }
            resetLayoutParams();
            Glide.clear(mGifImageView);
        }
    }

    public void resetLayoutParams() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mGifImageView.getLayoutParams();
        int height = getResources().getDimensionPixelOffset(R.dimen.gif_height);
        if (params.height > height) {
            params.height = height;
            mGifImageView.setLayoutParams(params);
        }
    }


    public void resetForRecycler() {
        clearGlide();
        mGifTitle.setText("");
        mGifImageView.setBackground(null);
        mStartedPlayGIfAnimation = false;
    }

    public void setGifItem(GifItem item) {
        this.gifItem = item;
        if (TextUtils.isEmpty(item.gif_title)) {
            mGifTitle.setVisibility(View.GONE);
        } else {
            mGifTitle.setVisibility(View.VISIBLE);
            mGifTitle.setText(item.gif_title);
        }
        simpleTarget = new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    try {
                        mGifImageView.setBackground(new BitmapDrawable(BlurFactory.getDefault().blur(resource)));
                    } catch (OutOfMemoryError error) {
                        System.gc();
                        System.gc();
                        System.gc();
                    }
                }
            }
        };
        imageLoader.load(gifItem.gif_url).asBitmap().
                diskCacheStrategy(DiskCacheStrategy.SOURCE).thumbnail(0.1f).into(simpleTarget);
    }

    public void startGifAnimation() {
        try {
            if (mStartedPlayGIfAnimation) {
                return;
            }
            mStartedPlayGIfAnimation = true;
            if (gifItem != null) {
                imageLoader.load(gifItem.gif_url).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mGifImageView);
            }
        } catch (OutOfMemoryError error) {
            System.gc();
            System.gc();
            System.gc();
        }
    }

}
