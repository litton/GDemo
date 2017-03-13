package com.fantasy.coolgif.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fantasy.coolgif.R;
import com.fantasy.coolgif.widget.GIfSingleView;

/**
 * Created by fanlitao on 17/3/13.
 */

public class GifItemViewHolder extends RecyclerView.ViewHolder {


    public ImageView mGifImageVIew;
    public TextView mTitleView;
    public GIfSingleView singleView;
    public ImageView mLikeImageView;
    public ImageView mHeartImageView;
    public View mSharedImageView;
    public TextView mLikeCountTv;
    public View mFullScreen;
    public GifItemViewHolder(View itemView) {
        super(itemView);
        singleView =(GIfSingleView)itemView;
        mGifImageVIew = (ImageView) itemView.findViewById(R.id.img);
        mTitleView = (TextView) itemView.findViewById(R.id.gif_title);
        singleView.setGifImageView(mGifImageVIew);
        singleView.setGifTitle(mTitleView);
        mLikeImageView = (ImageView) itemView.findViewById(R.id.like);
        mLikeCountTv = (TextView) itemView.findViewById(R.id.like_count);
        mHeartImageView =  (ImageView)itemView.findViewById(R.id.heart);
        mSharedImageView = itemView.findViewById(R.id.share);
        mFullScreen = itemView.findViewById(R.id.full_screen);

    }
}
