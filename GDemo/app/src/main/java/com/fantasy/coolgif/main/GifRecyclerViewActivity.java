package com.fantasy.coolgif.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fantasy.coolgif.adapter.MainGifRecyclerAdapter;
import com.fantasy.coolgif.network.INetworkCallback;
import com.fantasy.coolgif.network.NetworkBus;
import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.utils.LogUtil;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.widget.GIfSingleView;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;


public class GifRecyclerViewActivity extends AppCompatActivity implements INetworkCallback {


    private RecyclerView mGifPager;
    private MainGifRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mFirstVisiableItemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        NetworkBus.getDefault().getTopGifList(this);


        mGifPager = (RecyclerView) findViewById(R.id.list);
        mGifPager.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false);
        mGifPager.setLayoutManager(mLayoutManager);
        mGifPager.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        GIfSingleView view = (GIfSingleView) mGifPager.findViewWithTag(mFirstVisiableItemPosition);
                        if (view != null) {
                            view.startGifAnimation();
                        }

                        GIfSingleView view2 = (GIfSingleView) mGifPager.findViewWithTag(mFirstVisiableItemPosition + 1);
                        if (view2 != null) {
                            view2.startGifAnimation();
                        }
                        GIfSingleView view3 = (GIfSingleView) mGifPager.findViewWithTag(mFirstVisiableItemPosition + 2);
                        if (view3 != null) {
                            view3.startGifAnimation();
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mFirstVisiableItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                if (mFirstVisiableItemPosition == 0 && mGifPager.getChildAt(0).getTop() == 0) {
                    GIfSingleView view = (GIfSingleView) mGifPager.findViewWithTag(mFirstVisiableItemPosition);
                    if (view != null) {
                        view.startGifAnimation();
                    }

                    GIfSingleView view2 = (GIfSingleView) mGifPager.findViewWithTag(mFirstVisiableItemPosition + 1);
                    if (view2 != null) {
                        view2.startGifAnimation();
                    }

                } else if (mGifPager.getChildAt(0).getTop() < 0) {
                    mFirstVisiableItemPosition = mFirstVisiableItemPosition + 1;
                }
            }
        });
    }


    @Override
    public void onResponse(GifResponse response) {

        if (mAdapter == null) {
            mAdapter = new MainGifRecyclerAdapter(mGifPager,response.data);
            mGifPager.setAdapter(mAdapter);
        }
    }

    @Override
    public void onDeleteCompeletd(String gifUrl) {

    }

    /**
     * 清除内存缓存
     *
     * @param context
     */
    public void clearMemoryCache(Context context) {
        Glide.get(context).clearMemory();
    }

    /**
     * 清除磁盘缓存
     *
     * @param context
     */
    public void clearDiskCache(Context context) {
        Glide.get(context).clearDiskCache();
    }

    @Override
    protected void onDestroy() {


        LogUtil.v("fan", "onDestory");
        clearMemoryCache(GifRecyclerViewActivity.this);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                //clearDiskCache(MainActivity.this);
            }
        }).subscribeOn(Schedulers.io()).subscribe();


        super.onDestroy();
    }
}
