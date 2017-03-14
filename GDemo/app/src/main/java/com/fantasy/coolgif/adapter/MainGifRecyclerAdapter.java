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
import com.fantasy.coolgif.network.INetworkCallback;
import com.fantasy.coolgif.network.NetworkBus;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.response.LikeResponse;
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

    public MainGifRecyclerAdapter(RecyclerView rv, List<GifItem> data) {
        mDataList = data;
        mContext = MainApplication.getInstance().getApplicationContext();
        imageLoader = Glide.with(MainApplication.getInstance().getApplicationContext());
        mRecyclerView = rv;
    }


    public void addNewDataList(List<GifItem> dataList) {
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }


    @Override
    public GifItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.gif_item_wrap_content, parent, false);
        GifItemViewHolder holder = new GifItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final GifItemViewHolder holder, int position) {
        final GifItem item = mDataList.get(position);
        holder.singleView.setTag(position);
        holder.singleView.resetForRecycler();
        holder.singleView.setGifItem(item);

        if (item.like_info > 0) {
            holder.mLikeCountTv.setVisibility(View.VISIBLE);
            holder.mLikeCountTv.setText(String.valueOf(item.like_info));
        } else {
            holder.mLikeCountTv.setVisibility(View.INVISIBLE);
        }
        holder.mFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.mGifImageVIew.getLayoutParams();
                if (params.height == mRecyclerView.getHeight()) {
                    holder.singleView.resetLayoutParams();
                } else {
                    params.height = mRecyclerView.getHeight();
                    holder.mGifImageVIew.setLayoutParams(params);
                }

            }
        });
        holder.mLikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.mLikeImageView.isSelected()) {
                    int count = Integer.parseInt(holder.mLikeCountTv.getText().toString());
                    count += 1;
                    holder.mLikeCountTv.setText(String.valueOf(count));
                    holder.mLikeImageView.setSelected(true);
                    NetworkBus.getDefault().likeGifById(item.id, new NetworkBus.ILikeGifCallback() {
                        @Override
                        public void onSucessful(LikeResponse response) {
                            LogUtil.v("fan", "like.:" + response.id + ":" + response.like_info);
                        }
                    });
                }
            }
        });

        holder.mHeartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mHeartImageView.setSelected(true);
            }
        });

        holder.mSharedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkBus.getDefault().deleteGifById(item.id, new INetworkCallback() {
                    @Override
                    public void onResponse(GifResponse response) {

                    }

                    @Override
                    public void onDeleteCompeletd(int id) {
                        GifItem item = new GifItem();
                        item.id = id;
                        boolean result = mDataList.remove(item);
                        LogUtil.v("fan", "delete:" + result + ":" + id);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailed() {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
