package com.fantasy.coolgif.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fantasy.coolgif.blur.BlurFactory;
import com.fantasy.coolgif.main.MainApplication;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.utils.LogUtil;

import java.util.List;

/**
 * Created by fanlitao on 17/3/13.
 */

public class MainGifRecyclerAdapter extends RecyclerView.Adapter<GifItemViewHolder> {


    private List<GifItem> mDataList;
    private Context mContext;
    private RequestManager imageLoader;
    private RecyclerView mRecyclerView;
    public MainGifRecyclerAdapter(RecyclerView rv,List<GifItem> data) {
        mDataList = data;
        mContext = MainApplication.getInstance().getApplicationContext();
        imageLoader = Glide.with(MainApplication.getInstance().getApplicationContext());
        mRecyclerView = rv;

    }


    @Override
    public GifItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gif_item_wrap_content, parent, false);
        GifItemViewHolder holder = new GifItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GifItemViewHolder holder, int position) {
        GifItem item = mDataList.get(position);
        holder.singleView.setTag(position);
        holder.singleView.resetForRecycler();
        holder.singleView.setGifItem(item);
        holder.mFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.mGifImageVIew.getLayoutParams();
                params.height =mRecyclerView.getHeight();
                holder.mGifImageVIew.setLayoutParams(params);
            }
        });
        holder.mLikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.mLikeImageView.isSelected()) {
                    int count = Integer.parseInt(holder.mLikeCountTv.getText().toString());
                    count += 1;
                    holder.mLikeCountTv.setText(String.valueOf(count));
                    holder.mLikeImageView.setSelected(true);
                }
            }
        });

        holder.mHeartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mHeartImageView.setSelected(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
