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
import com.fantasy.coolgif.db.DBHelper;
import com.fantasy.coolgif.main.MainApplication;
import com.fantasy.coolgif.network.INetworkCallback;
import com.fantasy.coolgif.network.NetworkBus;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.response.LikeResponse;
import com.fantasy.coolgif.utils.AnalyticsEvent;
import com.fantasy.coolgif.utils.LogUtil;
import com.fantasy.coolgif.utils.Utils;

import java.util.List;

/**
 * Created by fanlitao on 17/3/13.
 */

public class MainGifRecyclerAdapter extends RecyclerView.Adapter<GifItemViewHolder> {


    private List<GifItem> mDataList;
    private Context mContext;
    private RequestManager imageLoader;
    private RecyclerView mRecyclerView;
    private boolean isSuperAccount;

    private DBHelper mDBHelper;

    public MainGifRecyclerAdapter(RecyclerView rv, List<GifItem> data) {
        mDataList = data;
        mContext = MainApplication.getInstance().getApplicationContext();
        imageLoader = Glide.with(MainApplication.getInstance().getApplicationContext());
        mRecyclerView = rv;
        isSuperAccount = Utils.isSuperAccount();
        mDBHelper = new DBHelper(mContext);
    }


    public void addNewDataList(List<GifItem> dataList) {
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public GifItem getItemByPosition(int position) {
        if (position > -1 && position < mDataList.size()) {
            return mDataList.get(position);
        }
        return null;
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
        holder.singleView.setTag(item.gif_url);
        holder.singleView.resetForRecycler();
        holder.singleView.setGifItem(item);
        holder.singleView.startGifAnimation();

        holder.mGifImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsEvent.onEvent("full_screen");
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.mGifImageVIew.getLayoutParams();
                if (params.height == mRecyclerView.getHeight()) {
                    holder.singleView.resetLayoutParams();
                }
            }
        });
        if (item.like_info > 0) {
            holder.mLikeCountTv.setVisibility(View.VISIBLE);
            holder.mLikeCountTv.setText(String.valueOf(item.like_info));
        } else {
            holder.mLikeCountTv.setVisibility(View.INVISIBLE);
        }
        holder.mLikeImageView.setSelected(mDBHelper.isHLikeddGifItem(item));
        holder.mHeartImageView.setSelected(mDBHelper.isHeartedGifItem(item));
        holder.mFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.mGifImageVIew.getLayoutParams();
                if (params.height == mRecyclerView.getHeight()) {
                    holder.singleView.resetLayoutParams();
                    AnalyticsEvent.onEvent("btn_scale_screen");
                } else {
                    AnalyticsEvent.onEvent("btn_full_screen");
                    params.height = mRecyclerView.getHeight();
                    holder.mGifImageVIew.setLayoutParams(params);
                }

            }
        });
        holder.mLikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSuperAccount) {
                    NetworkBus.getDefault().likeGifById(item.id, new NetworkBus.ILikeGifCallback() {
                        @Override
                        public void onSucessful(LikeResponse response) {
                            holder.mLikeCountTv.setText(String.valueOf(response.like_info));
                            holder.mLikeCountTv.setVisibility(View.VISIBLE);
                            item.like_info = response.like_info;
                        }
                    });
                } else {
                    if (!holder.mLikeImageView.isSelected()) {
                        holder.mLikeImageView.setSelected(true);
                        mDBHelper.saveLikeGif(item);
                        AnalyticsEvent.onEvent("like_gif_" + item.gif_url);
                        NetworkBus.getDefault().likeGifById(item.id, new NetworkBus.ILikeGifCallback() {
                            @Override
                            public void onSucessful(LikeResponse response) {
                                LogUtil.v("fan", "like.:" + response.id + ":" + response.like_info);
                                holder.mLikeCountTv.setText(String.valueOf(response.like_info));
                                holder.mLikeCountTv.setVisibility(View.VISIBLE);
                                item.like_info = response.like_info;
                            }
                        });
                    }
                }

            }
        });

        holder.mHeartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.mHeartImageView.isSelected()) {
                    holder.mHeartImageView.setSelected(false);
                    mDBHelper.deleteHeartGif(item);
                    AnalyticsEvent.onEvent("delete_heart_gif_" + item.gif_url);
                } else {
                    boolean result = mDBHelper.saveClickHeartGif(item);

                    AnalyticsEvent.onEvent("heart_gif_" + item.gif_url);

                    holder.mHeartImageView.setSelected(true);
                }

            }
        });

        if(isSuperAccount) {
            holder.mSharedImageView.setVisibility(View.VISIBLE);
            holder.mSharedImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSuperAccount) {
                        NetworkBus.getDefault().deleteGifById(item.id, new INetworkCallback() {
                            @Override
                            public void onResponse(GifResponse response) {

                            }

                            @Override
                            public void onDeleteCompeletd(int id) {
                                GifItem item = new GifItem();
                                item.id = id;
                                boolean result = mDataList.remove(item);
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailed() {

                            }
                        });
                    } else {

                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }
}
