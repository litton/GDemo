package com.fantasy.coolgif.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.fantasy.coolgif.R;
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
    }

    public void startGifAnimation() {
        LogUtil.v("fan", "startGifAnimation:");
        if (gifItem != null) {
            imageLoader.load(gifItem.gif_url).asGif().
                    diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mGifImageView);
        }
    }

}
