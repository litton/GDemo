package com.fantasy.coolgif.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fantasy.coolgif.R;
import com.fantasy.coolgif.adapter.MainGifRecyclerAdapter;
import com.fantasy.coolgif.db.DBHelper;
import com.fantasy.coolgif.response.GifItem;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * Created by fanlitao on 17/3/15.
 */

public class ViewGifsListActivity extends AppCompatActivity {


    public static final int ACTION_LIKE_TYPE = 0;
    public static final int ACTION_HEART_TYPE = 1;
    public static final String ACTION_TYPE = "action_type";
    private RecyclerView mGifPager;
    private MainGifRecyclerAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_main);

        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        findViewById(R.id.setting).setVisibility(View.GONE);
        Glide.with(this).resumeRequests();
        TextView titleView = (TextView) findViewById(R.id.title_string);
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
            }
        });
        DBHelper dbHelper = new DBHelper(this);
        int actionType = getIntent().getIntExtra(ACTION_TYPE, 0);
        List<GifItem> dataList = null;
        if (actionType == ACTION_LIKE_TYPE) {
            dataList = dbHelper.getAllLikedGIfList();
            titleView.setText(R.string.like_gif);

        } else {
            dataList = dbHelper.getAllHeartedGifList();
            titleView.setText(R.string.heart_gif);
        }


        mAdapter = new MainGifRecyclerAdapter(mGifPager, dataList);
        mGifPager.setAdapter(mAdapter);

    }
}
