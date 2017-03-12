package com.fantasy.coolgif.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
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
import com.fantasy.coolgif.widget.GIfSingleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanlitao on 17/3/12.
 */

public class MainPagerAdapter extends PagerAdapter {

    private List<GifItem> mDataList;
    private RequestManager imageLoader;
    private List<View> viewContainter = new ArrayList<View>();

    public MainPagerAdapter(List<GifItem> dataList) {
        mDataList = dataList;

        imageLoader = Glide.with(MainApplication.getInstance().getApplicationContext());
        // mItemView = LayoutInflater.from(MainApplication.getInstance().getApplicationContext()).inflate(R.layout.image_item,null);
    }


    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        LogUtil.v("fan","destoryItem:" + object + ":" + position);

        GIfSingleView view = (GIfSingleView)object;
        if(view != null) {
            view.clearGlide();
        }
        ((ViewPager) container).removeView(viewContainter.get(position));

    }


    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(View container, int position) {
        GifItem imageInfo = mDataList.get(position);
        GIfSingleView view = (GIfSingleView) LayoutInflater.from(MainApplication.getInstance().getApplicationContext()).inflate(R.layout.gif_item, null);
        view.setTag(position);
        view.setGifItem(imageInfo);
        if (position == 0) {
            view.startGifAnimation();
        }
        ((ViewPager) container).addView(view);
        viewContainter.add(view);
        return view;

    }

}
