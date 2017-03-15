package com.fantasy.coolgif.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.fantasy.coolgif.adapter.MainGifRecyclerAdapter;
import com.fantasy.coolgif.network.INetworkCallback;
import com.fantasy.coolgif.network.NetworkBus;
import com.fantasy.coolgif.response.GifItem;
import com.fantasy.coolgif.response.GifResponse;
import com.fantasy.coolgif.utils.LogUtil;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.utils.PreferenceUtil;
import com.fantasy.coolgif.widget.GIfSingleView;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;


public class GifRecyclerViewActivity extends AppCompatActivity implements INetworkCallback, View.OnClickListener {


    private RecyclerView mGifPager;
    private MainGifRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private int mFirstVisiableItemPosition;
    private int mLastVisiableItemPosition;
    public static final int PRE_REQUERY_COUNT = 3;

    private boolean mWaitingResponse = false;

    private int mInitRequestPosition;
    private int mLastSavePosition;

    public static final boolean SUPPORT_PRELOAD_GIF = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);

        MobclickAgent.setScenarioType(this,MobclickAgent.EScenarioType. E_UM_NORMAL);
        findViewById(R.id.setting).setOnClickListener(this);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        mInitRequestPosition = PreferenceUtil.getGifRequestPosition();
        Glide.with(this).resumeRequests();
        NetworkBus.getDefault().getTopGifList(mInitRequestPosition, this);
        mWaitingResponse = true;

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
                        Glide.with(getApplicationContext()).resumeRequests();


                        int newPos = mInitRequestPosition + mFirstVisiableItemPosition + 1;
                        if (newPos < mLastSavePosition) {
                            return;
                        }
                        PreferenceUtil.saveGifCurrentPos(mInitRequestPosition + mFirstVisiableItemPosition + 1);
                        mLastSavePosition = mInitRequestPosition + mFirstVisiableItemPosition + 1;

                        if (SUPPORT_PRELOAD_GIF) {
                            GifItem item = mAdapter.getItemByPosition(mLastVisiableItemPosition + 1);
                            if (item != null) {
                                if (!GifRecyclerViewActivity.this.isFinishing()) {
                                    Glide.with(GifRecyclerViewActivity.this).load(item.gif_url).downloadOnly(500, 500);
                                }

                            }

                            GifItem item2 = mAdapter.getItemByPosition(mLastVisiableItemPosition + 2);
                            if (item2 != null) {
                                if (!GifRecyclerViewActivity.this.isFinishing()) {
                                    Glide.with(GifRecyclerViewActivity.this).load(item2.gif_url).downloadOnly(500, 500);
                                }
                            }

                        }


                        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                        int visibleItemCount = layoutManager.getChildCount();
                        int totalItemCount = layoutManager.getItemCount();
                        if (!mWaitingResponse && (visibleItemCount > 0 && (mLastVisiableItemPosition) >= totalItemCount - PRE_REQUERY_COUNT)) {
                            NetworkBus.getDefault().getTopGifList(totalItemCount + mInitRequestPosition, GifRecyclerViewActivity.this);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        // Glide.with(getApplicationContext()).pauseRequests();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Glide.with(getApplicationContext()).pauseRequests();
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mFirstVisiableItemPosition = mLayoutManager.findFirstVisibleItemPosition();
                mLastVisiableItemPosition = mLayoutManager.findLastVisibleItemPosition();
                if (mGifPager.getChildAt(0).getTop() < 0) {
                    mFirstVisiableItemPosition = mFirstVisiableItemPosition + 1;
                }
            }
        });
    }


    @Override
    public void onResponse(GifResponse response) {
        mWaitingResponse = false;
        if (mAdapter == null) {
            LogUtil.v("fan", "onResponse:" + response);
            mAdapter = new MainGifRecyclerAdapter(mGifPager, response.data);
            mGifPager.setAdapter(mAdapter);
        } else {
            mAdapter.addNewDataList(response.data);
        }
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onDeleteCompeletd(int gifUrl) {

    }

    @Override
    public void onFailed() {
        mWaitingResponse = false;
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


        LogUtil.v("fan", "onDestory:" + PreferenceUtil.getGifRequestPosition());
        clearMemoryCache(GifRecyclerViewActivity.this);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                //clearDiskCache(MainActivity.this);
            }
        }).subscribeOn(Schedulers.io()).subscribe();


        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,SettingActivity.class);
        startActivity(intent);
    }
}
